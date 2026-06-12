package com.springboot.swmlopsbe.domain.prediction.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.prediction.entity.PredictionResult;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Long> {

    // 최근 24시간 중으로 예측한 유저 중 마케팅이 효과 있던 유저 선별
  @Query(
      "SELECT p FROM PredictionResult p WHERE p.isPersuadable = true AND p.createdAt >= :since "
          + "AND p.createdAt = (SELECT MAX(p2.createdAt) FROM PredictionResult p2 WHERE p2.user = p.user)")
  List<PredictionResult> findLatestPersuadableResultsSince(@Param("since") LocalDateTime since);

  @Query("SELECT p FROM PredictionResult p JOIN FETCH p.user ORDER BY p.createdAt DESC")
  List<PredictionResult> findAllWithUser();
}