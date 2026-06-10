package com.springboot.swmlopsbe.domain.log.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.swmlopsbe.domain.log.entity.CustomerLog;

public interface CustomerLogRepository extends JpaRepository<CustomerLog, Long> {}
