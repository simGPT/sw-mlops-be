package com.springboot.swmlopsbe.domain.prediction.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ModelUpliftResponse {

  private boolean success;
  private UpliftResult result;

  @Getter
  @Setter
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
  public static class UpliftResult {
    private double upliftScore;
    private boolean isPersuadable;
  }
}
