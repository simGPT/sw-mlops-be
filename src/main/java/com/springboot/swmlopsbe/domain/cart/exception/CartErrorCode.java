package com.springboot.swmlopsbe.domain.cart.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "장바구니 관련 에러 코드")
public enum CartErrorCode implements BaseErrorCode {
  @Schema(description = "존재하지 않는 장바구니 항목")
  CART_ITEM_NOT_FOUND("C001", "존재하지 않는 장바구니 항목입니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
