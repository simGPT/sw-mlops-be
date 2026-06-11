package com.springboot.swmlopsbe.domain.log.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.swmlopsbe.domain.log.entity.CustomerLog;
import com.springboot.swmlopsbe.domain.log.entity.EventType;
import com.springboot.swmlopsbe.domain.user.entity.User;

public interface CustomerLogRepository extends JpaRepository<CustomerLog, Long> {

    // 사용자 행동 로그 개수
  @Query(
      "SELECT COUNT(l) FROM CustomerLog l WHERE l.user = :user AND l.eventType = :eventType AND l.createdAt >= :since")
  long countByUserAndEventTypeAndCreatedAtAfter(
      @Param("user") User user,
      @Param("eventType") EventType eventType,
      @Param("since") LocalDateTime since);
}
