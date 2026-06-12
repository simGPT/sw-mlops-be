package com.springboot.swmlopsbe.domain.cart.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.cart.dto.request.CartAddRequest;
import com.springboot.swmlopsbe.domain.cart.dto.request.CartUpdateRequest;
import com.springboot.swmlopsbe.domain.cart.dto.response.CartResponse;
import com.springboot.swmlopsbe.domain.cart.service.CartService;
import com.springboot.swmlopsbe.global.common.BaseResponse;
import com.springboot.swmlopsbe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  @PostMapping("/items")
  @Operation(summary = "[토큰 O] 장바구니 상품 추가", description = "장바구니에 상품을 추가합니다. 이미 담긴 상품이면 수량을 합산합니다.")
  public ResponseEntity<BaseResponse<CartResponse>> addItem(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody CartAddRequest request) {
    return ResponseEntity.ok(
        BaseResponse.success(cartService.addItem(userDetails.getUser(), request)));
  }

  @GetMapping
  @Operation(summary = "[토큰 O] 장바구니 조회", description = "본인의 장바구니를 조회합니다.")
  public ResponseEntity<BaseResponse<CartResponse>> getCart(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(BaseResponse.success(cartService.getCart(userDetails.getUser())));
  }

  @PatchMapping("/items/{itemId}")
  @Operation(summary = "[토큰 O] 장바구니 수량 변경", description = "장바구니 항목의 수량을 변경합니다.")
  public ResponseEntity<BaseResponse<CartResponse>> updateQuantity(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Long itemId,
      @Valid @RequestBody CartUpdateRequest request) {
    return ResponseEntity.ok(
        BaseResponse.success(cartService.updateQuantity(userDetails.getUser(), itemId, request)));
  }

  @DeleteMapping("/items/{itemId}")
  @Operation(summary = "[토큰 O] 장바구니 항목 삭제", description = "장바구니에서 상품을 삭제합니다.")
  public ResponseEntity<BaseResponse<CartResponse>> removeItem(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long itemId) {
    return ResponseEntity.ok(
        BaseResponse.success(cartService.removeItem(userDetails.getUser(), itemId)));
  }
}
