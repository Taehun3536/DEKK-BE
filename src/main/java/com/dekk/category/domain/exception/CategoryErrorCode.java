package com.dekk.category.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    CATEGORY_NAME_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40001", "카테고리 이름은 필수값입니다"),
    CATEGORY_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "ECT40002", "카테고리 이름은 10자 이내여야 합니다"),
    PARENT_CATEGORY_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40003", "하위 카테고리 생성 시 상위 카테고리는 필수입니다"),
    ONLY_PARENT_CAN_HAVE_CHILDREN(HttpStatus.BAD_REQUEST, "ECT40004", "상위 카테고리만 하위 카테고리를 가질 수 있습니다"),
    ONLY_CHILD_CATEGORY_CAN_MAP_CARD(HttpStatus.BAD_REQUEST, "ECT40005", "하위 카테고리만 카드에 매핑할 수 있습니다"),
    CARD_ALREADY_IN_CATEGORY(HttpStatus.BAD_REQUEST, "ECT40006", "이미 해당 카테고리에 포함된 카드입니다"),
    CARD_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40007", "카드 ID는 필수값입니다"),
    CATEGORY_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ECT40008", "카테고리 ID는 필수값입니다"),

    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "ECT40401", "카테고리를 찾을 수 없습니다"),
    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "ECT40402", "카드를 찾을 수 없습니다");

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
