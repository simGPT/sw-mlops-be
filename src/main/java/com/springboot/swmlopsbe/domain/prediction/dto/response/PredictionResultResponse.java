package com.springboot.swmlopsbe.domain.prediction.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.springboot.swmlopsbe.domain.prediction.entity.PredictionResult;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "예측 결과 응답 DTO")
public class PredictionResultResponse {

  @Schema(description = "사용자 ID")
  private UUID userId;

  @Schema(description = "이탈 위험 여부", example = "true")
  private boolean churned;

  @Schema(description = "이탈 예측 신뢰도 (0.0 ~ 1.0)", example = "0.518")
  private double churnConfidence;

  @Schema(description = "업리프트 점수 (이탈 위험 유저만 계산, 아닌 경우 null)", example = "0.45")
  private Double upliftScore;

  @Schema(description = "마케팅 개입 효과 여부 (이탈 위험 유저만 계산)", example = "true")
  private boolean isPersuadable;

  @Schema(description = "예측 일시")
  private LocalDateTime predictedAt;

  public static PredictionResultResponse from(PredictionResult result) {
    return PredictionResultResponse.builder()
        .userId(result.getUser().getId())
        .churned(result.isChurned())
        .churnConfidence(result.getChurnConfidence())
        .upliftScore(result.getUpliftScore())
        .isPersuadable(result.isPersuadable())
        .predictedAt(result.getCreatedAt())
        .build();
  }
}
