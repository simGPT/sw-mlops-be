package com.springboot.swmlopsbe.domain.admin.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.admin.dto.request.AdminEmailRequest;
import com.springboot.swmlopsbe.domain.marketing.service.MarketingService;
import com.springboot.swmlopsbe.domain.prediction.dto.response.PredictionResultResponse;
import com.springboot.swmlopsbe.domain.prediction.repository.PredictionResultRepository;
import com.springboot.swmlopsbe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

  private final MarketingService marketingService;
  private final PredictionResultRepository predictionResultRepository;

  @PostMapping("/marketing/trigger")
  @Operation(summary = "[관리자] 이메일 마케팅 수동 트리거", description = "전체 유저 예측 후 설득 가능 유저에게만 이메일을 발송합니다.")
  public ResponseEntity<BaseResponse<Void>> triggerMarketing() {
    marketingService.executeMarketingCampaign();
    return ResponseEntity.ok(BaseResponse.success(null));
  }

  @PostMapping("/emails")
  @Operation(summary = "[관리자] 특정 유저에게 이메일 직접 발송")
  public ResponseEntity<BaseResponse<Void>> sendEmail(
      @RequestBody @Valid AdminEmailRequest request) {
    marketingService.sendEmailToUser(request.getUserId(), request.getSubject(), request.getBody());
    return ResponseEntity.ok(BaseResponse.success(null));
  }

  @GetMapping("/predictions")
  @Operation(summary = "[관리자] 전체 유저 예측 결과 조회", description = "전체 유저의 예측 결과를 최신순으로 반환합니다.")
  public ResponseEntity<BaseResponse<List<PredictionResultResponse>>> getAllPredictions() {
    List<PredictionResultResponse> results =
        predictionResultRepository.findAllWithUser().stream()
            .map(PredictionResultResponse::from)
            .toList();
    return ResponseEntity.ok(BaseResponse.success(results));
  }
}
