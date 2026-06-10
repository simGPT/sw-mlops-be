package com.springboot.swmlopsbe.domain.log.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.log.dto.request.LogRequest;
import com.springboot.swmlopsbe.domain.log.service.CustomerLogService;
import com.springboot.swmlopsbe.global.common.BaseResponse;
import com.springboot.swmlopsbe.global.security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class CustomerLogController {

  private final CustomerLogService customerLogService;

  @PostMapping
  @Operation(summary = "[토큰 O] 상품 조회 로그 기록", description = "상품 조회(VIEW) 이벤트를 로그에 기록합니다.")
  public ResponseEntity<BaseResponse<Void>> recordView(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @Valid @RequestBody LogRequest request) {
    customerLogService.recordView(userDetails.getUser(), request.getProductId());
    return ResponseEntity.ok(BaseResponse.success(null));
  }
}
