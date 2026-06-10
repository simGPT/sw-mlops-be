package com.springboot.swmlopsbe.domain.order.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.order.dto.request.OrderRequest;
import com.springboot.swmlopsbe.domain.order.dto.response.OrderResponse;
import com.springboot.swmlopsbe.domain.order.service.OrderService;
import com.springboot.swmlopsbe.global.common.BaseResponse;
import com.springboot.swmlopsbe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @Operation(summary = "[토큰 O] 주문 생성", description = "주문을 생성합니다.")
  public ResponseEntity<BaseResponse<OrderResponse>> createOrder(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody OrderRequest request) {
    OrderResponse response = orderService.createOrder(userDetails.getUser(), request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(HttpStatus.CREATED.value(), "주문이 완료되었습니다.", response));
  }

  @GetMapping
  @Operation(summary = "[토큰 O] 주문 목록 조회", description = "본인의 주문 목록을 조회합니다.")
  public ResponseEntity<BaseResponse<List<OrderResponse>>> getOrders(
      @AuthenticationPrincipal CustomUserDetails userDetails) {
    return ResponseEntity.ok(BaseResponse.success(orderService.getOrders(userDetails.getUser())));
  }

  @GetMapping("/{orderId}")
  @Operation(summary = "[토큰 O] 주문 상세 조회", description = "주문 상세 정보를 조회합니다.")
  public ResponseEntity<BaseResponse<OrderResponse>> getOrder(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable UUID orderId) {
    return ResponseEntity.ok(
        BaseResponse.success(orderService.getOrder(userDetails.getUser(), orderId)));
  }

  @PatchMapping("/{orderId}/items/{itemId}/return")
  @Operation(summary = "[토큰 O] 반품 처리", description = "주문 항목을 반품 처리합니다.")
  public ResponseEntity<BaseResponse<OrderResponse>> returnOrderItem(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable UUID orderId,
      @PathVariable Long itemId) {
    return ResponseEntity.ok(
        BaseResponse.success(orderService.returnOrderItem(userDetails.getUser(), orderId, itemId)));
  }
}
