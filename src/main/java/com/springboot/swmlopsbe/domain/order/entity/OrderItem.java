package com.springboot.swmlopsbe.domain.order.entity;

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
import com.springboot.swmlopsbe.global.common.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private int price;

  @Column private String imageUrl;

  @Column(nullable = false)
  private boolean isReturned;

  @Builder
  public OrderItem(Order order, Product product, int quantity, int price, String imageUrl) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.price = price;
    this.imageUrl = imageUrl;
    this.isReturned = false;
  }

  public void returnItem() {
    this.isReturned = true;
  }
}
