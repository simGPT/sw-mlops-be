package com.springboot.swmlopsbe.domain.prediction.dto.request;

import lombok.Getter;

@Getter
public class ChurnPredictRequest {

  private final String type = "churn";
  private final FeatureRequest data;

  public ChurnPredictRequest(FeatureRequest data) {
    this.data = data;
  }
}
