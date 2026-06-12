package com.springboot.swmlopsbe.domain.prediction.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FeatureRequest {

  private long accountAgeMonths;
  private double avgOrderValue;
  private long totalOrders;
  private long daysSinceLastPurchase;
  private double discountUsageRate;
  private double returnRate;
  private double browsingFrequencyPerWeek;
  private double cartAbandonmentRate;
}
