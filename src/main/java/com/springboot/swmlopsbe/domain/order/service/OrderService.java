package com.springboot.swmlopsbe.domain.order.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.cart.repository.CartItemRepository;
import com.springboot.swmlopsbe.domain.log.entity.EventType;
import com.springboot.swmlopsbe.domain.log.service.CustomerLogService;
import com.springboot.swmlopsbe.domain.order.dto.request.OrderRequest;
import com.springboot.swmlopsbe.domain.order.dto.response.OrderResponse;
import com.springboot.swmlopsbe.domain.order.entity.Order;
import com.springboot.swmlopsbe.domain.order.entity.OrderItem;
import com.springboot.swmlopsbe.domain.order.exception.OrderErrorCode;
import com.springboot.swmlopsbe.domain.order.repository.OrderItemRepository;
import com.springboot.swmlopsbe.domain.order.repository.OrderRepository;
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
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemRepository orderItemRepository;
  private final ProductRepository productRepository;
  private final CartItemRepository cartItemRepository;
  private final CustomerLogService customerLogService;

  @Transactional
  public OrderResponse createOrder(User user, OrderRequest request) {
    log.info("[주문 생성] 요청 - userId: {}", user.getId());

    int totalPrice = 0;

    Order order = Order.builder().user(user).totalPrice(0).discountAmount(0).build();
    orderRepository.save(order);

    for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
      Product product =
          productRepository
              .findById(itemRequest.getProductId())
              .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

      if (product.getStock() < itemRequest.getQuantity()) {
        log.warn("[주문 생성] 재고 부족 - productId: {}", product.getId());
        throw new CustomException(ProductErrorCode.OUT_OF_STOCK);
      }

      OrderItem orderItem =
          OrderItem.builder()
              .order(order)
              .product(product)
              .quantity(itemRequest.getQuantity())
              .price(product.getPrice())
              .build();

      orderItemRepository.save(orderItem);
      order.addOrderItem(orderItem);
      totalPrice += product.getPrice() * itemRequest.getQuantity();
    }

    // 재고 차감 및 구매 로그는 모든 항목 유효성 검증 후 일괄 처리
    for (OrderItem item : order.getOrderItems()) {
      item.getProduct().decreaseStock(item.getQuantity());
      customerLogService.record(user, item.getProduct(), EventType.PURCHASE);
    }

    cartItemRepository.deleteByUser(user); // 주문 생성 후에는 장바구니 비우기
    log.info("[주문 생성] 완료 - orderId: {}, totalPrice: {}", order.getId(), totalPrice);
    return OrderResponse.from(order);
  }

  @Transactional(readOnly = true)
  public List<OrderResponse> getOrders(User user) {
    log.info("[주문 목록 조회] 요청 - userId: {}", user.getId());
    return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
        .map(OrderResponse::from)
        .toList();
  }

  @Transactional(readOnly = true)
  public OrderResponse getOrder(User user, UUID orderId) {
    log.info("[주문 상세 조회] 요청 - userId: {}, orderId: {}", user.getId(), orderId);

    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new CustomException(OrderErrorCode.ORDER_FORBIDDEN);
    }

    return OrderResponse.from(order);
  }

  @Transactional
  public OrderResponse returnOrderItem(User user, UUID orderId, Long itemId) {
    log.info("[반품 처리] 요청 - userId: {}, orderId: {}, itemId: {}", user.getId(), orderId, itemId);

    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new CustomException(OrderErrorCode.ORDER_FORBIDDEN);
    }

    OrderItem orderItem =
        orderItemRepository
            .findByIdAndOrderId(itemId, orderId)
            .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_ITEM_NOT_FOUND));

    if (orderItem.isReturned()) {
      throw new CustomException(OrderErrorCode.ALREADY_RETURNED);
    }

    orderItem.returnItem();
    customerLogService.record(user, orderItem.getProduct(), EventType.RETURN);
    log.info("[반품 처리] 완료 - itemId: {}", itemId);

    return OrderResponse.from(order);
  }
}
