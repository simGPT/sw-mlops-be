package com.springboot.swmlopsbe.domain.log.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "행동 로그 기록 요청 (VIEW 이벤트)")
public class LogRequest {

  @NotNull
  @Schema(description = "조회한 상품 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID productId;
}
