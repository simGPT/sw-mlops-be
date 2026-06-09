package com.springboot.swmlopsbe.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

  private String secret;
  private long accessTokenValidityInSeconds;
  private long refreshTokenValidityInSeconds;
  private boolean secure;
  private String sameSite;
}
