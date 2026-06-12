package com.springboot.swmlopsbe.domain.product.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.swmlopsbe.domain.product.dto.response.ProductResponse;
import com.springboot.swmlopsbe.domain.product.service.ProductService;
import com.springboot.swmlopsbe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  @Operation(summary = "[토큰 X] 상품 목록 조회", description = "전체 상품 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<ProductResponse>>> getProducts() {
    return ResponseEntity.ok(BaseResponse.success(productService.getProducts()));
  }

  @GetMapping("/{productId}")
  @Operation(summary = "[토큰 X] 상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
  public ResponseEntity<BaseResponse<ProductResponse>> getProduct(@PathVariable UUID productId) {
    return ResponseEntity.ok(BaseResponse.success(productService.getProduct(productId)));
  }

  @PutMapping(value = "/{productId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "[토큰 O] 상품 이미지 업로드", description = "상품 이미지를 S3에 업로드합니다.")
  public ResponseEntity<BaseResponse<ProductResponse>> uploadImage(
      @PathVariable UUID productId, @RequestPart MultipartFile file) {
    return ResponseEntity.ok(BaseResponse.success(productService.uploadImage(productId, file)));
  }
}
