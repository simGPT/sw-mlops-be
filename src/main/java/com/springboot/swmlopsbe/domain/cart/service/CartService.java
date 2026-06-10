package com.springboot.swmlopsbe.domain.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.cart.dto.request.CartAddRequest;
import com.springboot.swmlopsbe.domain.cart.dto.request.CartUpdateRequest;
import com.springboot.swmlopsbe.domain.cart.dto.response.CartResponse;
import com.springboot.swmlopsbe.domain.cart.entity.CartItem;
import com.springboot.swmlopsbe.domain.cart.exception.CartErrorCode;
import com.springboot.swmlopsbe.domain.cart.repository.CartItemRepository;
import com.springboot.swmlopsbe.domain.log.entity.EventType;
import com.springboot.swmlopsbe.domain.log.service.CustomerLogService;
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
public class CartService {

  private final CartItemRepository cartItemRepository;
  private final ProductRepository productRepository;
  private final CustomerLogService customerLogService;

  @Transactional
  public CartResponse addItem(User user, CartAddRequest request) {
    log.info("[장바구니 추가] 요청 - userId: {}, productId: {}", user.getId(), request.getProductId());

    Product product =
        productRepository
            .findById(request.getProductId())
            .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

    cartItemRepository
        .findByUserAndProduct(user, product)
        .ifPresentOrElse(
            existing -> {
              existing.addQuantity(request.getQuantity());
              log.info("[장바구니 추가] 수량 합산 - itemId: {}", existing.getId());
            },
            () -> {
              CartItem newItem =
                  CartItem.builder()
                      .user(user)
                      .product(product)
                      .quantity(request.getQuantity())
                      .build();
              cartItemRepository.save(newItem);
              log.info("[장바구니 추가] 신규 항목 저장 - productId: {}", product.getId());
            });

    customerLogService.record(user, product, EventType.ADD_TO_CART);
    return CartResponse.from(cartItemRepository.findAllByUser(user));
  }

  @Transactional(readOnly = true)
  public CartResponse getCart(User user) {
    log.info("[장바구니 조회] 요청 - userId: {}", user.getId());
    List<CartItem> items = cartItemRepository.findAllByUser(user);
    return CartResponse.from(items);
  }

  @Transactional
  public CartResponse updateQuantity(User user, Long itemId, CartUpdateRequest request) {
    log.info("[장바구니 수량 변경] 요청 - userId: {}, itemId: {}", user.getId(), itemId);

    CartItem cartItem =
        cartItemRepository
            .findByIdAndUser(itemId, user)
            .orElseThrow(() -> new CustomException(CartErrorCode.CART_ITEM_NOT_FOUND));

    cartItem.updateQuantity(request.getQuantity());
    log.info("[장바구니 수량 변경] 완료 - itemId: {}, quantity: {}", itemId, request.getQuantity());

    return CartResponse.from(cartItemRepository.findAllByUser(user));
  }

  @Transactional
  public CartResponse removeItem(User user, Long itemId) {
    log.info("[장바구니 삭제] 요청 - userId: {}, itemId: {}", user.getId(), itemId);

    CartItem cartItem =
        cartItemRepository
            .findByIdAndUser(itemId, user)
            .orElseThrow(() -> new CustomException(CartErrorCode.CART_ITEM_NOT_FOUND));

    cartItemRepository.delete(cartItem);
    log.info("[장바구니 삭제] 완료 - itemId: {}", itemId);

    return CartResponse.from(cartItemRepository.findAllByUser(user));
  }

  @Transactional
  public void clearCart(User user) {
    log.info("[장바구니 비우기] 요청 - userId: {}", user.getId());
    cartItemRepository.deleteByUser(user);
  }
}
