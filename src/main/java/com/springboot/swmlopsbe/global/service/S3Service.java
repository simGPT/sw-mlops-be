package com.springboot.swmlopsbe.global.service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.swmlopsbe.global.exception.CustomException;
import com.springboot.swmlopsbe.global.exception.GlobalErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class S3Service {

  @Value("${app.base-url}")
  private String baseUrl;

  @Value("${app.uploads-dir:uploads}")
  private String uploadsDir;

  public String upload(MultipartFile file, String directory) throws IOException {
    String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
    Path targetDir = Paths.get(uploadsDir, directory);
    Files.createDirectories(targetDir);
    Files.write(targetDir.resolve(fileName), file.getBytes());
    String relativePath = directory + "/" + fileName;
    log.info("[Local] 업로드 완료 - path: {}", relativePath);
    return relativePath;
  }

  public void delete(String relativePath) {
    if (relativePath == null || relativePath.startsWith("https://")) return;
    try {
      Files.deleteIfExists(Paths.get(uploadsDir, relativePath));
      log.info("[Local] 삭제 완료 - path: {}", relativePath);
    } catch (IOException e) {
      log.warn("[Local] 삭제 실패 - path: {}", relativePath);
    }
  }

  public String generatePresignedUrl(String imageUrl) {
    if (imageUrl == null || imageUrl.startsWith("https://")) return null;
    if (imageUrl.startsWith("/images/")) {
      String filename = imageUrl.substring("/images/".length());
      String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
      return baseUrl + "/images/" + encodedFilename;
    }
    String encodedKey = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8).replace("+", "%20");
    return baseUrl + "/api/images?key=" + encodedKey;
  }

  public byte[] downloadAsBytes(String key) {
    log.info("[Local] 파일 읽기 - key: '{}'", key);
    try {
      return Files.readAllBytes(Paths.get(uploadsDir, key));
    } catch (IOException e) {
      log.warn("[Local] 파일 없음 - key: '{}'", key);
      throw new CustomException(GlobalErrorCode.RESOURCE_NOT_FOUND);
    }
  }
}
