package com.springboot.swmlopsbe.global.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.swmlopsbe.global.config.property.S3Properties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Client s3Client;
  private final S3Properties s3Properties;

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
}
