package com.springboot.swmlopsbe.domain.prediction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.prediction.entity.PredictionResult;

public interface PredictionResultRepository extends JpaRepository<PredictionResult, Long> {}
