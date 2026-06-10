package com.springboot.swmlopsbe.domain.auth.service;

import java.time.LocalDateTime;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.swmlopsbe.domain.auth.dto.request.LoginRequest;
import com.springboot.swmlopsbe.domain.auth.dto.request.SignupRequest;
import com.springboot.swmlopsbe.domain.auth.dto.response.LoginResponse;
import com.springboot.swmlopsbe.domain.auth.dto.response.SignupResponse;
import com.springboot.swmlopsbe.domain.auth.entity.RefreshToken;
import com.springboot.swmlopsbe.domain.auth.exception.AuthErrorCode;
import com.springboot.swmlopsbe.domain.auth.repository.RefreshTokenRepository;
import com.springboot.swmlopsbe.domain.user.entity.User;
import com.springboot.swmlopsbe.domain.user.repository.UserRepository;
import com.springboot.swmlopsbe.global.exception.CustomException;
import com.springboot.swmlopsbe.global.security.jwt.JwtProvider;
import com.springboot.swmlopsbe.global.security.jwt.TokenType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Value("${jwt.refresh-token-validity-in-seconds}")
  private long refreshTokenValidityInSeconds;

  @Transactional
  public SignupResponse signup(SignupRequest request) {
    log.info("[회원가입] 요청 - username: {}", request.getUsername());

    if (userRepository.existsByUsername(request.getUsername())) {
      log.warn("[회원가입] 중복 아이디 - username: {}", request.getUsername());
      throw new CustomException(AuthErrorCode.DUPLICATE_USERNAME);
    }

    User user =
        User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .build();

    userRepository.save(user);
    log.info("[회원가입] 완료 - username: {}, userId: {}", user.getUsername(), user.getId());

    return SignupResponse.from(user);
  }

  @Transactional
  public LoginResult login(LoginRequest request) {
    log.info("[로그인] 요청 - username: {}", request.getUsername());

    User user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(
                () -> {
                  log.warn("[로그인] 존재하지 않는 사용자 - username: {}", request.getUsername());
                  return new CustomException(AuthErrorCode.USER_NOT_FOUND);
                });

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      log.warn("[로그인] 비밀번호 불일치 - username: {}", request.getUsername());
      throw new CustomException(AuthErrorCode.INVALID_PASSWORD);
    }

    String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getUsername());
    String refreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getUsername());

    refreshTokenRepository.deleteByUser(user);
    refreshTokenRepository.save(
        RefreshToken.builder()
            .user(user)
            .token(refreshToken)
            .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInSeconds))
            .build());

    log.info("[로그인] 완료 - username: {}, userId: {}", user.getUsername(), user.getId());

    return new LoginResult(accessToken, refreshToken, LoginResponse.from(user));
  }

  @Transactional
  public LoginResult refresh(HttpServletRequest request) {
    String refreshToken = jwtProvider.extractRefreshToken(request);

    if (refreshToken == null) {
      log.warn("[토큰 재발급] Refresh Token 없음");
      throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    jwtProvider.validateToken(refreshToken, TokenType.REFRESH_TOKEN);

    RefreshToken saved =
        refreshTokenRepository
            .findByToken(refreshToken)
            .orElseThrow(
                () -> {
                  log.warn("[토큰 재발급] DB에 존재하지 않는 Refresh Token");
                  return new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
                });

    if (saved.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("[토큰 재발급] 만료된 Refresh Token - userId: {}", saved.getUser().getId());
      refreshTokenRepository.delete(saved);
      throw new CustomException(AuthErrorCode.EXPIRED_REFRESH_TOKEN);
    }

    User user = saved.getUser();
    String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getUsername());
    String newRefreshToken = jwtProvider.generateRefreshToken(user.getId(), user.getUsername());

    refreshTokenRepository.delete(saved);
    refreshTokenRepository.save(
        RefreshToken.builder()
            .user(user)
            .token(newRefreshToken)
            .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInSeconds))
            .build());

    log.info("[토큰 재발급] 완료 - username: {}, userId: {}", user.getUsername(), user.getId());

    return new LoginResult(newAccessToken, newRefreshToken, LoginResponse.from(user));
  }

  @Transactional
  public void logout(HttpServletRequest request) {
    String accessToken = jwtProvider.extractAccessToken(request);

    if (accessToken == null) {
      log.warn("[로그아웃] Access Token 없음");
      throw new CustomException(AuthErrorCode.INVALID_REFRESH_TOKEN);
    }

    String username = jwtProvider.getUsernameFromToken(accessToken);
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new CustomException(AuthErrorCode.USER_NOT_FOUND));

    refreshTokenRepository.deleteByUser(user);
    log.info("[로그아웃] 완료 - username: {}, userId: {}", user.getUsername(), user.getId());
  }

  // login 함수에서 한번에 accessToken, refreshToken, response를 넘기기 위한 내부 데이터 운반용 Record
  public record LoginResult(String accessToken, String refreshToken, LoginResponse response) {}
}
