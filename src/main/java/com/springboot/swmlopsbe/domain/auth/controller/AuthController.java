package com.springboot.swmlopsbe.domain.auth.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.auth.dto.request.SignupRequest;
import com.springboot.swmlopsbe.domain.auth.dto.response.SignupResponse;
import com.springboot.swmlopsbe.domain.auth.service.AuthService;
import com.springboot.swmlopsbe.global.common.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  @Operation(summary = "[토큰 X] 회원가입", description = "회원가입 API")
  public ResponseEntity<BaseResponse<SignupResponse>> signup(
      @Valid @RequestBody SignupRequest request) {
    SignupResponse response = authService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", response));
  }
}
