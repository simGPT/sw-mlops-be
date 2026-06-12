package com.springboot.swmlopsbe.domain.order.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "주문 관련 에러 코드")
public enum OrderErrorCode implements BaseErrorCode {
  @Schema(description = "존재하지 않는 주문")
  ORDER_NOT_FOUND("O001", "존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND),

  @Schema(description = "존재하지 않는 주문 항목")
  ORDER_ITEM_NOT_FOUND("O002", "존재하지 않는 주문 항목입니다.", HttpStatus.NOT_FOUND),

  @Schema(description = "본인 주문 아님")
  ORDER_FORBIDDEN("O003", "본인의 주문만 조회할 수 있습니다.", HttpStatus.FORBIDDEN),

  @Schema(description = "이미 반품된 상품")
  ALREADY_RETURNED("O004", "이미 반품 처리된 상품입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
