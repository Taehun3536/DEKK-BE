package com.dekk.activelog.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ActiveLogErrorCode implements ErrorCode {
    USER_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40001", "사용자 ID는 필수 값입니다"),
    CARD_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40002", "카드 ID는 필수 값입니다"),
    SWIPE_TYPE_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EAL40003", "스와이프 타입은 필수 값입니다"),

    ACTIVE_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "EAL40401", "스와이프 이력을 찾을 수 없습니다"),

    ALREADY_SWIPED(HttpStatus.CONFLICT, "EAL40901", "이미 스와이프 평가를 완료한 카드입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
