package com.springboot.swmlopsbe.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.domain.auth.dto.request.LoginRequest;
import com.springboot.swmlopsbe.domain.auth.dto.request.SignupRequest;
import com.springboot.swmlopsbe.domain.auth.dto.response.LoginResponse;
import com.springboot.swmlopsbe.domain.auth.dto.response.SignupResponse;
import com.springboot.swmlopsbe.domain.auth.service.AuthService;
import com.springboot.swmlopsbe.domain.auth.service.AuthService.LoginResult;
import com.springboot.swmlopsbe.global.common.BaseResponse;
import com.springboot.swmlopsbe.global.security.jwt.JwtProvider;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final JwtProvider jwtProvider;

  @PostMapping("/signup")
  @Operation(summary = "[토큰 X] 회원가입", description = "회원가입 API")
  public ResponseEntity<BaseResponse<SignupResponse>> signup(
      @Valid @RequestBody SignupRequest request) {
    SignupResponse response = authService.signup(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", response));
  }

  @PostMapping("/login")
  @Operation(
      summary = "[토큰 X] 로그인",
      description = "로그인 API: Access/Refresh Token을 HttpOnly Cookie로 발급")
  public ResponseEntity<BaseResponse<LoginResponse>> login(
      @Valid @RequestBody LoginRequest request) {
    LoginResult result = authService.login(request);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            jwtProvider.createAccessTokenCookie(result.accessToken()).toString())
        .header(
            HttpHeaders.SET_COOKIE,
            jwtProvider.createRefreshTokenCookie(result.refreshToken()).toString())
        .body(BaseResponse.success("로그인이 완료되었습니다.", result.response()));
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "[Refresh Token] 토큰 재발급",
      description = "Refresh Token Cookie로 Access/Refresh Token을 재발급합니다.")
  public ResponseEntity<BaseResponse<LoginResponse>> refresh(HttpServletRequest request) {
    LoginResult result = authService.refresh(request);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.SET_COOKIE,
            jwtProvider.createAccessTokenCookie(result.accessToken()).toString())
        .header(
            HttpHeaders.SET_COOKIE,
            jwtProvider.createRefreshTokenCookie(result.refreshToken()).toString())
        .body(BaseResponse.success("토큰이 재발급되었습니다.", result.response()));
  }

  @PostMapping("/logout")
  @Operation(
      summary = "[토큰 O] 로그아웃",
      description = "로그아웃 API: Cookie를 만료시키고 Refresh Token을 DB에서 삭제합니다.")
  public ResponseEntity<BaseResponse<Void>> logout(HttpServletRequest request) {
    authService.logout(request);

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, jwtProvider.clearAccessTokenCookie().toString())
        .header(HttpHeaders.SET_COOKIE, jwtProvider.clearRefreshTokenCookie().toString())
        .body(BaseResponse.success("로그아웃이 완료되었습니다.", null));
  }
}
