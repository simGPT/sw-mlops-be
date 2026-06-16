package com.springboot.swmlopsbe.domain.abtest.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "AB 테스트 관련 에러 코드")
public enum AbTestErrorCode implements BaseErrorCode {
  @Schema(description = "추출 가능한 확정 데이터 없음")
  NO_CONFIRMED_OUTCOME("AB001", "추출 가능한 AB 테스트 확정 데이터가 없습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
