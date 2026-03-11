package com.dekk.security.oauth2.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OAuth2ErrorCode implements ErrorCode {
    MISSING_USER_INFO(HttpStatus.BAD_REQUEST, "EA40001", "소셜 로그인 제공자로부터 필수 사용자 정보를 받지 못했습니다."),
    UNSUPPORTED_PROVIDER(HttpStatus.BAD_REQUEST, "EA40002", "지원하지 않는 소셜 로그인 제공자입니다."),

    OAUTH2_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "EA40003", "소셜 로그인 인증에 실패했습니다.");

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
