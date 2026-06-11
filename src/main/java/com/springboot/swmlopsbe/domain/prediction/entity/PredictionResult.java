package com.springboot.swmlopsbe.domain.prediction.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@Table(name = "prediction_results")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PredictionResult {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private boolean churned;

  @Column(nullable = false)
  private double churnConfidence;

  // 이탈 위험 유저만 Uplift 계산 → 아닌 경우 null
  @Column private Double upliftScore;

  @Column(nullable = false)
  private boolean isPersuadable; // (마케팅 시에 구매 or 마케팅 안할 시에 이탈)할 유저인지 여부에 대한 필드

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  public PredictionResult(
      User user,
      boolean churned,
      double churnConfidence,
      Double upliftScore,
      boolean isPersuadable) {
    this.user = user;
    this.churned = churned;
    this.churnConfidence = churnConfidence;
    this.upliftScore = upliftScore;
    this.isPersuadable = isPersuadable;
  }
}
