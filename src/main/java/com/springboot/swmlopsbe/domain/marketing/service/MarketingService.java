package com.springboot.swmlopsbe.domain.marketing.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.springboot.swmlopsbe.domain.abtest.service.AbTestService;
import com.springboot.swmlopsbe.domain.auth.exception.AuthErrorCode;
import com.springboot.swmlopsbe.domain.prediction.dto.request.FeatureRequest;
import com.springboot.swmlopsbe.domain.prediction.service.PredictionService;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.domain.user.repository.UserRepository;
import com.springboot.swmlopsbe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarketingService {

  private final UserRepository userRepository;
  private final PredictionService predictionService;
  private final EmailService emailService;
  private final AbTestService abTestService;

  // 마케팅 이메일 전송하는 함수
  public void executeMarketingCampaign() {
    log.info("[마케팅 캠페인 시작]");
    List<User> users = userRepository.findAll();
    int count = 0;

    for (User user : users) {
      try {
        FeatureRequest features = predictionService.calculateFeatures(user);
        var result = predictionService.predict(user, features);

        if (result.isChurned()) {
          abTestService.recordAssignment(user, features, result.isPersuadable());
        }

        if (result.isPersuadable()) {
          emailService.sendEmail(
              user.getEmail(),
              String.format("🌴 %s님, 여름 특별 세일 혜택을 놓치고 계신 건 아닌가요?", user.getName()),
              String.format(
                  "%s님, 한동안 만나 뵙지 못했네요.\n\n"
                      + "현재 여름 특별 세일이 진행 중이며, %s님을 위한 다양한 할인 혜택이 준비되어 있습니다.\n\n"
                      + "특별한 혜택과 함께 예쁜 화장품 받아보세요!",
                  user.getName(), user.getName()));
          count++;
        }
      } catch (Exception e) {
        log.error("[마케팅 캠페인 실패] userId: {}, error: {}", user.getId(), e.getMessage());
      }
    }

    log.info("[마케팅 캠페인 완료] 이메일 발송 수: {}", count);
  }

  public void sendEmailToUser(UUID userId, String subject, String body) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));
    emailService.sendEmail(user.getEmail(), subject, body);
  }
}
