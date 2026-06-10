package com.springboot.swmlopsbe.domain.order.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.order.entity.Order;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface OrderRepository extends JpaRepository<Order, UUID> {

  List<Order> findByUserOrderByCreatedAtDesc(User user);
}
