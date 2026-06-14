package com.springboot.swmlopsbe.global.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.swmlopsbe.global.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final S3Service s3Service;

  @GetMapping
  public ResponseEntity<byte[]> getImage(@RequestParam String key) {
    log.info("[이미지 프록시] key: '{}'", key);
    byte[] bytes = s3Service.downloadAsBytes(key);

    MediaType mediaType = MediaType.IMAGE_JPEG;
    String lowerKey = key.toLowerCase();
    if (lowerKey.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
    else if (lowerKey.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;
    else if (lowerKey.endsWith(".webp")) mediaType = MediaType.parseMediaType("image/webp");

    return ResponseEntity.ok()
        .contentType(mediaType)
        .header("Cache-Control", "public, max-age=86400")
        .body(bytes);
  }
}
