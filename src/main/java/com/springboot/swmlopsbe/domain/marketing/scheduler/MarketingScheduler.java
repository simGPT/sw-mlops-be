package com.springboot.swmlopsbe.domain.marketing.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.springboot.swmlopsbe.domain.marketing.service.MarketingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MarketingScheduler {

  private final MarketingService marketingService;

  // 자정마다 스케줄러 작동 : 자정마다 전체 유저의 uplift 예측에 따라 isPersuadable = true 면 마케팅 이메일 발송하는 스케줄러
  @Scheduled(cron = "0 0 0 * * *")
  public void run() {
    marketingService.executeMarketingCampaign();
  }
}
