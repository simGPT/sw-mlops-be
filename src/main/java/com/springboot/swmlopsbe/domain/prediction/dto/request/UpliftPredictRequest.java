package com.springboot.swmlopsbe.domain.prediction.dto.request;

import lombok.Getter;

@Getter
public class UpliftPredictRequest {

  private final String type = "uplift";
  private final FeatureRequest data;

  public UpliftPredictRequest(FeatureRequest data) {
    this.data = data;
  }
}
