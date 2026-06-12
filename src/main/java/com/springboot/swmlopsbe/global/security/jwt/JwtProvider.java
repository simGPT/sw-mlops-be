package com.springboot.swmlopsbe.global.security.jwt;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.springboot.swmlopsbe.global.config.property.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final JwtProperties jwtProperties;

  private static final String ACCESS_TOKEN_COOKIE = "accessToken";
  private static final String REFRESH_TOKEN_COOKIE = "refreshToken";

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(UUID userId, String username) {
    return generateToken(
        userId, username, TokenType.ACCESS_TOKEN, jwtProperties.getAccessTokenValidityInSeconds());
  }

  public String generateRefreshToken(UUID userId, String username) {
    return generateToken(
        userId,
        username,
        TokenType.REFRESH_TOKEN,
        jwtProperties.getRefreshTokenValidityInSeconds());
  }

  private String generateToken(
      UUID userId, String username, TokenType tokenType, long validitySeconds) {
    Instant now = Instant.now();

    return Jwts.builder()
        .subject(username)
        .claim("userId", userId.toString())
        .claim("type", tokenType.name())
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(validitySeconds)))
        .signWith(getSigningKey())
        .compact();
  }

  public boolean validateToken(String token, TokenType tokenType) {
    Claims claims = extractClaims(token);
    String type = claims.get("type", String.class);

    if (!tokenType.name().equals(type)) {
      throw new JwtException("토큰 타입이 일치하지 않습니다.");
    }

    return true;
  }

  public UUID getUserIdFromToken(String token) {
    return UUID.fromString(extractClaims(token).get("userId", String.class));
  }

  public String getUsernameFromToken(String token) {
    return extractClaims(token).getSubject();
  }

  private Claims extractClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public ResponseCookie createAccessTokenCookie(String token) {
    return ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/")
        .maxAge(Duration.ofSeconds(jwtProperties.getAccessTokenValidityInSeconds()))
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public ResponseCookie createRefreshTokenCookie(String token) {
    return ResponseCookie.from(REFRESH_TOKEN_COOKIE, token)
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/api/auth")
        .maxAge(Duration.ofSeconds(jwtProperties.getRefreshTokenValidityInSeconds()))
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public ResponseCookie clearAccessTokenCookie() {
    return ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/")
        .maxAge(0)
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public ResponseCookie clearRefreshTokenCookie() {
    return ResponseCookie.from(REFRESH_TOKEN_COOKIE, "")
        .httpOnly(true)
        .secure(jwtProperties.isSecure())
        .path("/api/auth")
        .maxAge(0)
        .sameSite(jwtProperties.getSameSite())
        .build();
  }

  public String extractAccessToken(HttpServletRequest request) {
    var cookie = WebUtils.getCookie(request, ACCESS_TOKEN_COOKIE);
    return cookie != null ? cookie.getValue() : null;
  }

  public String extractRefreshToken(HttpServletRequest request) {
    var cookie = WebUtils.getCookie(request, REFRESH_TOKEN_COOKIE);
    return cookie != null ? cookie.getValue() : null;
  }
}
