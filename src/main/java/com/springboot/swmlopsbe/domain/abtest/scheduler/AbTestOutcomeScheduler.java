package com.springboot.swmlopsbe.domain.abtest.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.springboot.swmlopsbe.domain.abtest.service.AbTestService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AbTestOutcomeScheduler {

  private final AbTestService abTestService;

  // 매일 새벽 1시 - 마케팅 스케줄러(자정) 이후 실행하여 30일 지난 outcome 확정
  @Scheduled(cron = "0 0 1 * * *")
  public void run() {
    abTestService.measureOutcomes();
  }
}
