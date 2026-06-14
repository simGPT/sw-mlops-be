package com.springboot.swmlopsbe.global.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.swmlopsbe.global.config.property.S3Properties;
import com.springboot.swmlopsbe.global.exception.CustomException;
import com.springboot.swmlopsbe.global.exception.GlobalErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;
  private final S3Properties s3Properties;

  @Value("${app.base-url}")
  private String baseUrl;

  public String upload(MultipartFile file, String directory) throws IOException {
    String fileName = directory + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

    s3Client.putObject(
        PutObjectRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(fileName)
            .contentType(file.getContentType())
            .contentLength(file.getSize())
            .build(),
        RequestBody.fromBytes(file.getBytes()));

    String fileUrl =
        "https://"
            + s3Properties.getBucket()
            + ".s3."
            + s3Properties.getRegion()
            + ".amazonaws.com/"
            + fileName;

    log.info("[S3] 업로드 완료 - url: {}", fileUrl);
    return fileUrl;
  }

  public void delete(String fileUrl) {
    String key = fileUrl.substring(fileUrl.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());

    s3Client.deleteObject(
        DeleteObjectRequest.builder().bucket(s3Properties.getBucket()).key(key).build());

    log.info("[S3] 삭제 완료 - key: {}", key);
  }

  public String generatePresignedUrl(String s3Url) {
    if (s3Url == null) return null;
    String key = s3Url.substring(s3Url.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
    String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8).replace("+", "%20");
    return baseUrl + "/api/images?key=" + encodedKey;
  }

  public byte[] downloadAsBytes(String key) {
    log.info("[S3] 다운로드 시도 - bucket: {}, key: '{}'", s3Properties.getBucket(), key);
    try {
      ResponseBytes<GetObjectResponse> response =
          s3Client.getObjectAsBytes(
              GetObjectRequest.builder().bucket(s3Properties.getBucket()).key(key).build());
      return response.asByteArray();
    } catch (NoSuchKeyException e) {
      log.warn("[S3] 파일 없음 - key: '{}'", key);
      throw new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND);
    }
  }
}
