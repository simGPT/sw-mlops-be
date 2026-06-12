package com.springboot.swmlopsbe.domain.cart.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 상품 추가 요청")
public class CartAddRequest {

  @NotNull
  @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productId;

  @Min(1)
  @Schema(description = "수량", example = "1")
  private int quantity;
}
