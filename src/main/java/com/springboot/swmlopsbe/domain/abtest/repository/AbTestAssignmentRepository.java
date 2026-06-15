package com.springboot.swmlopsbe.domain.abtest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.abtest.entity.AbTestAssignment;

public interface AbTestAssignmentRepository extends JpaRepository<AbTestAssignment, Long> {}
