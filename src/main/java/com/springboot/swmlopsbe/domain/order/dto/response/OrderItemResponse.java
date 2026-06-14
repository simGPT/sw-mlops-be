package com.springboot.swmlopsbe.domain.order.dto.response;

import java.util.UUID;

import com.springboot.swmlopsbe.domain.order.entity.OrderItem;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "OrderItemResponse 주문 항목 응답 DTO")
public class OrderItemResponse {

  @Schema(description = "주문 항목 ID", example = "1")
  private Long itemId;

  @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productId;

  @Schema(description = "상품명", example = "롬앤 쥬시 래스팅 틴트")
  private String productName;

  @Schema(description = "수량", example = "2")
  private int quantity;

  @Schema(description = "주문 당시 가격", example = "9500")
  private int price;

  @Schema(description = "상품 이미지 URL", example = "https://...")
  private String imageUrl;

  @Schema(description = "반품 여부", example = "false")
  private boolean isReturned;

  public static OrderItemResponse from(OrderItem item) {
    return OrderItemResponse.builder()
        .itemId(item.getId())
        .productId(item.getProduct().getId())
        .productName(item.getProduct().getName())
        .quantity(item.getQuantity())
        .price(item.getPrice())
        .imageUrl(item.getImageUrl())
        .isReturned(item.isReturned())
        .build();
  }
}
