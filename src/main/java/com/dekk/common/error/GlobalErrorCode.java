package com.dekk.common.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "EG001", "잘못된 요청입니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "EG002", "요청 본문을 읽을 수 없습니다. JSON 형식을 확인해주세요."),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "EG003", "필수 요청 파라미터가 누락되었습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EG004", "예기치 못한 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() {
        return status;
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
