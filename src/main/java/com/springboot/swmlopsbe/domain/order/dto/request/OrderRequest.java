package com.springboot.swmlopsbe.domain.order.dto.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "OrderRequest 주문 요청 DTO")
public class OrderRequest {

  @NotEmpty(message = "주문 항목은 최소 1개 이상이어야 합니다.")
  @Schema(description = "주문 항목 목록")
  private List<OrderItemRequest> items;

  @Getter
  @NoArgsConstructor
  @Schema(title = "OrderItemRequest 주문 항목 DTO")
  public static class OrderItemRequest {

    @NotNull(message = "상품 ID는 필수입니다.")
    @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID productId;

    @Min(value = 1, message = "수량은 1개 이상이어야 합니다.")
    @Schema(description = "수량", example = "2")
    private int quantity;
  }
}
