package com.springboot.swmlopsbe.domain.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.cart.entity.CartItem;
import com.springboot.swmlopsbe.domain.product.entity.Product;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findAllByUser(User user);

  Optional<CartItem> findByUserAndProduct(User user, Product product);

  Optional<CartItem> findByIdAndUser(Long id, User user);

  void deleteByUser(User user);
}
