package com.springboot.swmlopsbe.domain.marketing.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.Body;
import software.amazon.awssdk.services.sesv2.model.Content;
import software.amazon.awssdk.services.sesv2.model.Destination;
import software.amazon.awssdk.services.sesv2.model.EmailContent;
import software.amazon.awssdk.services.sesv2.model.Message;
import software.amazon.awssdk.services.sesv2.model.SendEmailRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

  private final SesV2Client sesV2Client;

  @Value("${ses.sender}")
  private String sender;

  public void sendEmail(String to, String subject, String body) {
    try {
      sesV2Client.sendEmail(
          SendEmailRequest.builder()
              .fromEmailAddress(sender)
              .destination(Destination.builder().toAddresses(to).build())
              .content(
                  EmailContent.builder()
                      .simple(
                          Message.builder()
                              .subject(Content.builder().data(subject).charset("UTF-8").build())
                              .body(
                                  Body.builder()
                                      .text(
                                          Content.builder().data(body).charset("UTF-8").build())
                                      .build())
                              .build())
                      .build())
              .build());
      log.info("[이메일 발송 완료] to: {}", to);
    } catch (Exception e) {
      log.error("[이메일 발송 실패] to: {}, error: {}", to, e.getMessage());
      throw e;
    }
  }
}
