package com.springboot.swmlopsbe.domain.product.dto.response;

import java.util.UUID;

import com.springboot.swmlopsbe.domain.product.entity.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ProductResponse 상품 응답 DTO")
public class ProductResponse {

  @Schema(description = "상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @Schema(description = "상품명", example = "나이키 에어맥스")
  private String name;

  @Schema(description = "가격", example = "150000")
  private int price;

  @Schema(description = "재고", example = "50")
  private int stock;

  @Schema(
      description = "상품 이미지 URL",
      example = "https://sw-mlops-bucket.s3.ap-northeast-2.amazonaws.com/products/image.jpg")
  private String imageUrl;

  public static ProductResponse from(Product product) {
    return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .stock(product.getStock())
        .imageUrl(product.getImageUrl())
        .build();
  }
}
