package com.dekk.user.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ALREADY_ONBOARDED(HttpStatus.BAD_REQUEST, "EU40001", "이미 온보딩이 완료된 사용자입니다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "EU40002", "유효하지 않은 사용자 ID입니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "EU40003", "닉네임은 2자 이상 10자 이내여야 하며, 특수문자는 '_'만 허용됩니다."),
    INVALID_PROFILE_INFO(HttpStatus.BAD_REQUEST, "EU40004", "키와 몸무게는 필수 입력값입니다."),
    INVALID_PROFILE_HEIGHT(HttpStatus.BAD_REQUEST, "EU40006", "키는 100cm 이상 220cm 이하이어야 합니다."),
    INVALID_PROFILE_WEIGHT(HttpStatus.BAD_REQUEST, "EU40006", "몸무게는 30kg 이상 150kg 이하여야 합니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "EU40401", "사용자를 찾을 수 없습니다."),

    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "EU40901", "이미 사용 중인 닉네임입니다.");

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
