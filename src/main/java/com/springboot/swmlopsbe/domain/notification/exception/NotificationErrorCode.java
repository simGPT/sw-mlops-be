package com.springboot.swmlopsbe.domain.notification.exception;

import org.springframework.http.HttpStatus;

import com.springboot.swmlopsbe.global.exception.model.BaseErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "알림 관련 에러 코드")
public enum NotificationErrorCode implements BaseErrorCode {
    @Schema(description = "알림을 찾을 수 없음")
    NOTIFICATION_NOT_FOUND("NT001", "알림을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    @Schema(description = "알림 접근 권한 없음")
    NOTIFICATION_ACCESS_DENIED("NT002", "해당 알림에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
