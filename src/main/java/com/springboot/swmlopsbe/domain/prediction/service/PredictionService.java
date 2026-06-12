package com.springboot.swmlopsbe.domain.prediction.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.swmlopsbe.domain.log.entity.EventType;
import com.springboot.swmlopsbe.domain.log.repository.CustomerLogRepository;
import com.springboot.swmlopsbe.domain.order.repository.OrderRepository;
import com.springboot.swmlopsbe.domain.prediction.dto.request.ChurnPredictRequest;
import com.springboot.swmlopsbe.domain.prediction.dto.request.FeatureRequest;
import com.springboot.swmlopsbe.domain.prediction.dto.request.UpliftPredictRequest;
import com.springboot.swmlopsbe.domain.prediction.dto.response.ModelChurnResponse;
import com.springboot.swmlopsbe.domain.prediction.dto.response.ModelUpliftResponse;
import com.springboot.swmlopsbe.domain.prediction.dto.response.PredictionResultResponse;
import com.springboot.swmlopsbe.domain.prediction.entity.PredictionResult;
import com.springboot.swmlopsbe.domain.prediction.exception.PredictionErrorCode;
import com.springboot.swmlopsbe.domain.prediction.repository.PredictionResultRepository;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PredictionService {

  private static final int WINDOW_DAYS = 30;

  private final CustomerLogRepository customerLogRepository;
  private final OrderRepository orderRepository;
  private final PredictionResultRepository predictionResultRepository;
  private final WebClient modelApiClient;
  private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

  @Transactional
  public PredictionResultResponse predict(User user) {
    log.info("[예측 요청] userId: {}", user.getId());

    FeatureRequest features = calculateFeatures(user);

    ModelChurnResponse.ChurnResult churnResult = callChurnApi(features);
    log.info(
        "[Churn 예측 완료] userId: {}, churned: {}, confidence: {}",
        user.getId(),
        churnResult.isChurned(),
        churnResult.getConfidence());

    Double upliftScore = null;
    boolean isPersuadable = false;

    if (churnResult.isChurned()) {
      ModelUpliftResponse.UpliftResult upliftResult = callUpliftApi(features);
      upliftScore = upliftResult.getUpliftScore();
      isPersuadable = upliftResult.isPersuadable();
      log.info(
          "[Uplift 예측 완료] userId: {}, upliftScore: {}, isPersuadable: {}",
          user.getId(),
          upliftScore,
          isPersuadable);
    } else {
      log.info("[Uplift 스킵] userId: {} 이탈 위험 없음", user.getId());
    }

    PredictionResult result =
        predictionResultRepository.save(
            PredictionResult.builder()
                .user(user)
                .churned(churnResult.isChurned())
                .churnConfidence(churnResult.getConfidence())
                .upliftScore(upliftScore)
                .isPersuadable(isPersuadable)
                .build());

    return PredictionResultResponse.from(result);
  }

  private FeatureRequest calculateFeatures(User user) {
    LocalDateTime since = LocalDateTime.now().minusDays(WINDOW_DAYS);

    long accountAgeMonths = ChronoUnit.MONTHS.between(user.getCreatedAt(), LocalDateTime.now());
    double avgOrderValue = orderRepository.avgTotalPriceByUser(user);
    long totalOrders = orderRepository.countByUser(user);
    long daysSinceLastPurchase =
        orderRepository
            .findTopByUserOrderByCreatedAtDesc(user)
            .map(o -> ChronoUnit.DAYS.between(o.getCreatedAt(), LocalDateTime.now()))
            .orElse(999L);

    long discountedOrders = orderRepository.countDiscountedOrdersByUser(user);
    double discountUsageRate = totalOrders > 0 ? (double) discountedOrders / totalOrders : 0.0;

    long returnCount = countLog(user, EventType.RETURN, since);
    long purchaseCount = countLog(user, EventType.PURCHASE, since);
    long viewCount = countLog(user, EventType.VIEW, since);
    long cartAddCount = countLog(user, EventType.ADD_TO_CART, since);

    double returnRate = purchaseCount > 0 ? (double) returnCount / purchaseCount : 0.0;
    double browsingFrequencyPerWeek = viewCount * 7.0 / WINDOW_DAYS;
    double cartAbandonmentRate =
        cartAddCount > purchaseCount ? (double) (cartAddCount - purchaseCount) / cartAddCount : 0.0;

    return FeatureRequest.builder()
        .accountAgeMonths(accountAgeMonths)
        .avgOrderValue(avgOrderValue)
        .totalOrders(totalOrders)
        .daysSinceLastPurchase(daysSinceLastPurchase)
        .discountUsageRate(discountUsageRate)
        .returnRate(returnRate)
        .browsingFrequencyPerWeek(browsingFrequencyPerWeek)
        .cartAbandonmentRate(cartAbandonmentRate)
        .build();
  }

  private long countLog(User user, EventType eventType, LocalDateTime since) {
    return customerLogRepository.countByUserAndEventTypeAndCreatedAtAfter(user, eventType, since);
  }

  private <T> T callModelApi(
      Object request, String uri, Class<T> responseType, PredictionErrorCode errorCode) {
    try {
      String requestJson = objectMapper.writeValueAsString(request);
      String responseJson =
          modelApiClient
              .post()
              .uri(uri)
              .contentType(MediaType.APPLICATION_JSON)
              .bodyValue(requestJson)
              .retrieve()
              .bodyToMono(String.class)
              .block();
      return objectMapper.readValue(responseJson, responseType);
    } catch (CustomException e) {
      throw e;
    } catch (Exception e) {
      log.error(
          "[Model API 호출 실패] uri: {}, {} - {}", uri, e.getClass().getSimpleName(), e.getMessage());
      throw new CustomException(errorCode);
    }
  }

  private ModelChurnResponse.ChurnResult callChurnApi(FeatureRequest features) {
    ModelChurnResponse response =
        callModelApi(
            new ChurnPredictRequest(features),
            "/churn_predict",
            ModelChurnResponse.class,
            PredictionErrorCode.CHURN_API_ERROR);

    if (!response.isSuccess() || response.getResult() == null) {
      throw new CustomException(PredictionErrorCode.CHURN_API_ERROR);
    }
    return response.getResult();
  }

  private ModelUpliftResponse.UpliftResult callUpliftApi(FeatureRequest features) {
    ModelUpliftResponse response =
        callModelApi(
            new UpliftPredictRequest(features),
            "/uplift_predict",
            ModelUpliftResponse.class,
            PredictionErrorCode.UPLIFT_API_ERROR);

    if (!response.isSuccess() || response.getResult() == null) {
      throw new CustomException(PredictionErrorCode.UPLIFT_API_ERROR);
    }
    return response.getResult();
  }
}
