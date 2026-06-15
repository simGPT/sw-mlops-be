package com.springboot.swmlopsbe.domain.abtest.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.springboot.swmlopsbe.domain.user.entity.User;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ab_test_outcomes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AbTestOutcome {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "assignment_id", nullable = false)
  private AbTestAssignment assignment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private boolean converted = false;

  @Column private LocalDateTime convertedAt;

  @Column private LocalDateTime measuredAt;

  @Builder
  public AbTestOutcome(AbTestAssignment assignment, User user) {
    this.assignment = assignment;
    this.user = user;
    this.converted = false;
  }

  public void convert(LocalDateTime now) {
    if (!this.converted) {
      this.converted = true;
      this.convertedAt = now;
    }
  }

  public void measure(LocalDateTime now) {
    this.measuredAt = now;
  }
}
