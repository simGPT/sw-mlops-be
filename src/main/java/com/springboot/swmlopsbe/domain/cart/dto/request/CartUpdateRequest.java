package com.springboot.swmlopsbe.domain.cart.dto.request;

import jakarta.validation.constraints.Min;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 수량 변경 요청")
public class CartUpdateRequest {

  @Min(1)
  @Schema(description = "변경할 수량", example = "2")
  private int quantity;
}
