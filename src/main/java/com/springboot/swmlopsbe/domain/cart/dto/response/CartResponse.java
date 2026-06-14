package com.springboot.swmlopsbe.domain.cart.dto.response;

import java.util.List;
import java.util.function.Function;

import com.springboot.swmlopsbe.domain.cart.entity.CartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "장바구니 응답 DTO")
public class CartResponse {

  @Schema(description = "장바구니 항목 목록")
  private List<CartItemResponse> items;

  @Schema(description = "총 금액", example = "98000")
  private int totalPrice;

  public static CartResponse from(
      List<CartItem> cartItems, Function<String, String> urlTransformer) {
    List<CartItemResponse> itemResponses =
        cartItems.stream()
            .map(
                item ->
                    CartItemResponse.from(
                        item, urlTransformer.apply(item.getProduct().getImageUrl())))
            .toList();
    int totalPrice = itemResponses.stream().mapToInt(CartItemResponse::getSubtotal).sum();
    return CartResponse.builder().items(itemResponses).totalPrice(totalPrice).build();
  }
}
