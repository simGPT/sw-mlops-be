package com.springboot.swmlopsbe.domain.log.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.log.entity.CustomerLog;
import com.springboot.swmlopsbe.domain.log.entity.EventType;
import com.springboot.swmlopsbe.domain.log.repository.CustomerLogRepository;
import com.springboot.swmlopsbe.domain.product.entity.Product;
import com.springboot.swmlopsbe.domain.product.exception.ProductErrorCode;
import com.springboot.swmlopsbe.domain.product.repository.ProductRepository;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.global.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerLogService {

  private final CustomerLogRepository customerLogRepository;
  private final ProductRepository productRepository;

  @Transactional
  public void record(User user, Product product, EventType eventType) {
    customerLogRepository.save(
        CustomerLog.builder().user(user).product(product).eventType(eventType).build());
    log.info("[행동 로그] userId: {}, productId: {}, eventType: {}", user.getId(), product.getId(), eventType);
  }

  @Transactional
  public void recordView(User user, UUID productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
    record(user, product, EventType.VIEW);
  }
}
