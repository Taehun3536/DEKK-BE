package com.dekk.card.domain.exception;

import com.dekk.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

public enum CardErrorCode implements ErrorCode {
    PRODUCT_RAW_DATA_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40001", "상품 원본 데이터는 필수 값입니다"),
    PRODUCT_ORIGIN_URL_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40002", "상품 원본 이미지 경로는 필수 값입니다"),
    PRODUCT_NAME_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40003", "상품 이름은 필수값입니다"),
    PRODUCT_EXTERNAL_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40004", "상품 고유 id는 필수값입니다"),
    CARD_RAW_DATA_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40005", "카드 원본 데이터는 필수 값입니다"),
    CARD_ORIGIN_URL_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40006", "카드 원본 이미지 경로는 필수 값입니다"),
    CARD_ORIGIN_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40007", "카드 고유 id는 필수 값입니다"),
    CARD_PLATFORM_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "EC40008", "카드 플랫폼은 필수 값입니다"),
    CANNOT_CHANGE_STATUS_OF_DELETE_REQUESTED(HttpStatus.BAD_REQUEST, "EC40009", "삭제 요청된 카드의 상태는 변경할 수 없습니다"),

    CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "EC40401", "카드를 찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CardErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

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
