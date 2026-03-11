package com.dekk.auth.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "EA40101", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40102", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "EA40103", "지원되지 않는 JWT 토큰입니다."),
    EMPTY_CLAIMS(HttpStatus.UNAUTHORIZED, "EA40104", "JWT 클레임 문자열이 비어 있습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "EA40105", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "EA40107", "잘못된 타입의 JWT 토큰 유형입니다."),

    ABNORMAL_TOKEN_ACCESS(HttpStatus.FORBIDDEN, "EA40301", "비정상적인 토큰 접근이 감지되었습니다. 모든 기기에서 로그아웃됩니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "EA40901", "이미 다른 소셜 계정으로 가입된 이메일입니다.");

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
