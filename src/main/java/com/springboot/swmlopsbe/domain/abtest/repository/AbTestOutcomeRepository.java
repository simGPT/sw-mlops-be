package com.springboot.swmlopsbe.domain.abtest.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.swmlopsbe.domain.abtest.entity.AbTestOutcome;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface AbTestOutcomeRepository extends JpaRepository<AbTestOutcome, Long> {

  // 유저의 활성 outcome (배정 후 30일 이내, 아직 미확정)
  List<AbTestOutcome> findByUserAndMeasuredAtIsNullAndAssignment_AssignedAtAfter(
      User user, LocalDateTime since);

  // 30일 지났으나 아직 measuredAt 미확정인 outcome (스케줄러용)
  List<AbTestOutcome> findByMeasuredAtIsNullAndAssignment_AssignedAtBefore(LocalDateTime cutoff);

  // outcome 확정 완료된 데이터 전체 (재학습 데이터 추출용)
  @Query("SELECT o FROM AbTestOutcome o JOIN FETCH o.assignment WHERE o.measuredAt IS NOT NULL")
  List<AbTestOutcome> findAllConfirmedWithAssignment();
}
