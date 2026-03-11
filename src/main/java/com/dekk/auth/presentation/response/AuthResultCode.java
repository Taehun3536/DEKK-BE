package com.dekk.auth.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthResultCode implements ResultCode {
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "SA20001", "토큰 재발급 성공"),
    LOGOUT_SUCCESS(HttpStatus.OK, "SA20002", "로그아웃 성공");

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
