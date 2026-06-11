package com.springboot.swmlopsbe.domain.prediction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.prediction.dto.response.PredictionResultResponse;
import com.springboot.swmlopsbe.domain.prediction.service.PredictionService;
import com.springboot.swmlopsbe.global.common.BaseResponse;
import com.springboot.swmlopsbe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/predictions")
@RequiredArgsConstructor
public class PredictionController {

  private final PredictionService predictionService;

  // Churn 값이 true 일때는 Uplift 예측 값 반환, Churn 값이 false 일때는 Uplift 예측 값 null
  @PostMapping
  @Operation(
      summary = "[토큰 O] Churn/Uplift 예측 요청",
      description = "최근 30일 행동 로그 기반으로 이탈 및 업리프트 예측을 요청합니다.")
  public ResponseEntity<BaseResponse<PredictionResultResponse>> predict(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(
        BaseResponse.success(predictionService.predict(userDetails.getUser())));
  }
}
