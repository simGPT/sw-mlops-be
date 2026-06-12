package com.springboot.swmlopsbe.domain.cart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.springboot.swmlopsbe.domain.product.entity.Product;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Builder
  public CartItem(User user, Product product, int quantity) {
    this.user = user;
    this.product = product;
    this.quantity = quantity;
  }

  public void updateQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void addQuantity(int quantity) {
    this.quantity += quantity;
  }
}
