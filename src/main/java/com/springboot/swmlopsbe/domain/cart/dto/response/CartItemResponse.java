package com.springboot.swmlopsbe.domain.cart.dto.response;

import java.util.UUID;

import com.springboot.swmlopsbe.domain.cart.entity.CartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "장바구니 항목 응답 DTO")
public class CartItemResponse {

  @Schema(description = "장바구니 항목 ID", example = "1")
  private Long itemId;

  @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productId;

  @Schema(description = "상품명", example = "헤라 블랙 쿠션")
  private String productName;

  @Schema(description = "상품 단가", example = "49000")
  private int price;

  @Schema(description = "수량", example = "2")
  private int quantity;

  @Schema(description = "소계 (단가 × 수량)", example = "98000")
  private int subtotal;

  public static CartItemResponse from(CartItem cartItem) {
    return CartItemResponse.builder()
        .itemId(cartItem.getId())
        .productId(cartItem.getProduct().getId())
        .productName(cartItem.getProduct().getName())
        .price(cartItem.getProduct().getPrice())
        .quantity(cartItem.getQuantity())
        .subtotal(cartItem.getProduct().getPrice() * cartItem.getQuantity())
        .build();
  }
}
