package com.springboot.swmlopsbe.domain.product.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "상품 관련 에러 코드")
public enum ProductErrorCode implements BaseErrorCode {
  @Schema(description = "존재하지 않는 상품")
  PRODUCT_NOT_FOUND("P001", "존재하지 않는 상품입니다.", HttpStatus.NOT_FOUND),

  @Schema(description = "재고 부족")
  OUT_OF_STOCK("P002", "재고가 부족합니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
