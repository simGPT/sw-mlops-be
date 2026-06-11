package com.springboot.swmlopsbe.domain.prediction.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "예측 관련 에러 코드")
public enum PredictionErrorCode implements BaseErrorCode {
  @Schema(description = "Churn 모델 API 호출 실패")
  CHURN_API_ERROR("ML001", "Churn 예측 서버와 통신 중 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE),

  @Schema(description = "Uplift 모델 API 호출 실패")
  UPLIFT_API_ERROR("ML002", "Uplift 예측 서버와 통신 중 오류가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
