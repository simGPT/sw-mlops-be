package com.springboot.swmlopsbe.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "LoginRequest 로그인 요청 DTO")
public class LoginRequest {

  @NotBlank(message = "아이디는 필수입니다.")
  @Schema(description = "사용자 아이디", example = "simGPT")
  private String username;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Schema(description = "사용자 비밀번호", example = "simGPT123!")
  private String password;
}
