package com.springboot.swmlopsbe.domain.auth.dto.response;

import java.util.UUID;

import com.springboot.swmlopsbe.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "SignupResponse 회원가입 응답 DTO")
public class SignupResponse {

  @Schema(description = "사용자 ID", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID userId;

  @Schema(description = "사용자 아이디", example = "simGPT")
  private String username;

  @Schema(description = "사용자 이름", example = "심서현")
  private String name;

  public static SignupResponse from(User user) {
    return SignupResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .name(user.getName())
        .build();
  }
}
