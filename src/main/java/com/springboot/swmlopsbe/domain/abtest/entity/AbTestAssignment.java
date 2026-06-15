package com.springboot.swmlopsbe.domain.abtest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.springboot.swmlopsbe.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ab_test_assignments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class AbTestAssignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private GroupType groupType;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime assignedAt;

  // 배정 시점 피처 스냅샷
  @Column(nullable = false)
  private long accountAgeMonths;

  @Column(nullable = false)
  private double avgOrderValue;

  @Column(nullable = false)
  private long totalOrders;

  @Column(nullable = false)
  private long daysSinceLastPurchase;

  @Column(nullable = false)
  private double discountUsageRate;

  @Column(nullable = false)
  private double returnRate;

  @Column(nullable = false)
  private double browsingFrequencyPerWeek;

  @Column(nullable = false)
  private double cartAbandonmentRate;

  @Builder
  public AbTestAssignment(
      User user,
      GroupType groupType,
      long accountAgeMonths,
      double avgOrderValue,
      long totalOrders,
      long daysSinceLastPurchase,
      double discountUsageRate,
      double returnRate,
      double browsingFrequencyPerWeek,
      double cartAbandonmentRate) {
    this.user = user;
    this.groupType = groupType;
    this.accountAgeMonths = accountAgeMonths;
    this.avgOrderValue = avgOrderValue;
    this.totalOrders = totalOrders;
    this.daysSinceLastPurchase = daysSinceLastPurchase;
    this.discountUsageRate = discountUsageRate;
    this.returnRate = returnRate;
    this.browsingFrequencyPerWeek = browsingFrequencyPerWeek;
    this.cartAbandonmentRate = cartAbandonmentRate;
  }
}
