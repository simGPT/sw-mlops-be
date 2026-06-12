package com.springboot.swmlopsbe.domain.order.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.swmlopsbe.domain.order.entity.Order;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  List<Order> findByUserOrderByCreatedAtDesc(User user);

  // 전체 주문 수
  long countByUser(User user);

  // 평균 주문 금액
  @Query("SELECT COALESCE(AVG(o.totalPrice), 0.0) FROM Order o WHERE o.user = :user")
  double avgTotalPriceByUser(@Param("user") User user);

  // 할인 사용 비율
  @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user AND o.discountAmount > 0")
  long countDiscountedOrdersByUser(@Param("user") User user);

  // 마지막 구매 후 경과일
  Optional<Order> findTopByUserOrderByCreatedAtDesc(User user);
}
