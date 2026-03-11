package com.dekk.admin.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    INVALID_ADMIN_DATA(HttpStatus.BAD_REQUEST, "EADM40001", "관리자 필수 정보가 누락되었거나 올바르지 않습니다."),

    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "EADM40101", "비밀번호가 일치하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "EADM40102", "유효하지 않은 관리자 토큰입니다."),

    UNAUTHORIZED_ROLE(HttpStatus.FORBIDDEN, "EADM40301", "권한이 없습니다."),

    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "EADM40401", "관리자를 찾을 수 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "EADM40901", "이미 등록된 이메일입니다.");

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
