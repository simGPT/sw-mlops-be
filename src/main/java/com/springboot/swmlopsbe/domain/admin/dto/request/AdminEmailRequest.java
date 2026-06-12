package com.springboot.swmlopsbe.domain.admin.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class AdminEmailRequest {

  @NotNull private UUID userId;

  @NotBlank private String subject;

  @NotBlank private String body;
}
