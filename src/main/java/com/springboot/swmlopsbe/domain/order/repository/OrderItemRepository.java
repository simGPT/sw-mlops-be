package com.springboot.swmlopsbe.domain.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  Optional<OrderItem> findByIdAndOrderId(Long itemId, java.util.UUID orderId);
}
