package com.springboot.swmlopsbe.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springboot.swmlopsbe.global.config.property.S3Properties;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@Configuration
@RequiredArgsConstructor
public class SesConfig {

  private final S3Properties s3Properties;

  @Bean
  public SesV2Client sesV2Client() {
    return SesV2Client.builder()
        .region(Region.of(s3Properties.getRegion()))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    s3Properties.getAccessKey(), s3Properties.getSecretKey())))
        .build();
  }
}
