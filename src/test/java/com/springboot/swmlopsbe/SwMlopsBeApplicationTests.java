package com.springboot.swmlopsbe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sesv2.SesV2Client;

@SpringBootTest
class SwMlopsBeApplicationTests {

  @MockitoBean S3Client s3Client;

  @MockitoBean SesV2Client sesV2Client;

  @Test
  void contextLoads() {}
}
