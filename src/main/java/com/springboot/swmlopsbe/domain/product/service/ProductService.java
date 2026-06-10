package com.springboot.swmlopsbe.domain.product.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.swmlopsbe.domain.product.dto.response.ProductResponse;
import com.springboot.swmlopsbe.domain.product.entity.Product;
import com.springboot.swmlopsbe.domain.product.exception.ProductErrorCode;
import com.springboot.swmlopsbe.domain.product.repository.ProductRepository;
import com.springboot.swmlopsbe.global.exception.CustomException;
import com.springboot.swmlopsbe.global.exception.GlobalErrorCode;
import com.springboot.swmlopsbe.global.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final S3Service s3Service;

  @Transactional(readOnly = true)
  public List<ProductResponse> getProducts() {
    log.info("[상품 목록 조회] 요청");
    return productRepository.findAll().stream().map(ProductResponse::from).toList();
  }

  @Transactional(readOnly = true)
  public ProductResponse getProduct(UUID productId) {
    log.info("[상품 상세 조회] 요청 - productId: {}", productId);
    return productRepository
        .findById(productId)
        .map(ProductResponse::from)
        .orElseThrow(
            () -> {
              log.warn("[상품 상세 조회] 존재하지 않는 상품 - productId: {}", productId);
              return new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND);
            });
  }

  @Transactional
  public ProductResponse uploadImage(UUID productId, MultipartFile file) {
    log.info("[상품 이미지 업로드] 요청 - productId: {}", productId);

    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

    if (product.getImageUrl() != null) {
      s3Service.delete(product.getImageUrl());
    }

    try {
      String imageUrl = s3Service.upload(file, "products");
      product.updateImageUrl(imageUrl);
      log.info("[상품 이미지 업로드] 완료 - productId: {}, url: {}", productId, imageUrl);
      return ProductResponse.from(product);
    } catch (IOException e) {
      log.error("[상품 이미지 업로드] 실패 - productId: {}", productId, e);
      throw new CustomException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
