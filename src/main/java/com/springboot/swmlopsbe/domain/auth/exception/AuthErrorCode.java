package com.springboot.swmlopsbe.domain.auth.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "인증 관련 에러 코드")
public enum AuthErrorCode implements BaseErrorCode {
  @Schema(description = "이미 사용 중인 아이디")
  DUPLICATE_USERNAME("A001", "이미 사용 중인 아이디입니다.", HttpStatus.CONFLICT),

  @Schema(description = "존재하지 않는 사용자")
  USER_NOT_FOUND("A002", "존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND),

  @Schema(description = "비밀번호 불일치")
  INVALID_PASSWORD("A003", "비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),

  @Schema(description = "만료된 Refresh Token")
  EXPIRED_REFRESH_TOKEN("A004", "Refresh Token이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

  @Schema(description = "유효하지 않은 Refresh Token")
  INVALID_REFRESH_TOKEN("A005", "유효하지 않은 Refresh Token입니다.", HttpStatus.UNAUTHORIZED);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
