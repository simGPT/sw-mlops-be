package com.springboot.swmlopsbe.domain.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.springboot.swmlopsbe.domain.order.entity.Order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "OrderResponse 주문 응답 DTO")
public class OrderResponse {

  @Schema(description = "주문 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID orderId;

  @Schema(description = "총 금액", example = "19000")
  private int totalPrice;

  @Schema(description = "할인 금액", example = "0")
  private int discountAmount;

  @Schema(description = "주문 일시")
  private LocalDateTime createdAt;

  @Schema(description = "주문 항목 목록")
  private List<OrderItemResponse> items;

  public static OrderResponse from(Order order, Function<String, String> urlTransformer) {
    return OrderResponse.builder()
        .orderId(order.getId())
        .totalPrice(order.getTotalPrice())
        .discountAmount(order.getDiscountAmount())
        .createdAt(order.getCreatedAt())
        .items(
            order.getOrderItems().stream()
                .map(item -> OrderItemResponse.from(item, urlTransformer.apply(item.getImageUrl())))
                .toList())
        .build();
  }
}
