package com.dekk.deck.domain.exception;

import com.dekk.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeckErrorCode implements ErrorCode {
    USER_ID_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "ED40001", "유저 ID는 필수값입니다"),
    DECK_NAME_IS_REQUIRED_TO_CREATE(HttpStatus.BAD_REQUEST, "ED40002", "보관함 이름은 필수값입니다"),
    DECK_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ED40003", "보관함 ID는 필수값입니다"),
    CARD_ID_IS_REQUIRED(HttpStatus.BAD_REQUEST, "ED40004", "카드 ID는 필수값입니다"),
    CUSTOM_DECK_NAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "ED40005", "커스텀 보관함 이름은 1자 이상 15자 이내여야 합니다"),
    CUSTOM_DECK_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40006", "커스텀 보관함은 최대 8개까지 생성할 수 있습니다"),
    DEFAULT_DECK_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "ED40007", "기본 보관함은 수정하거나 삭제할 수 없습니다"),
    CUSTOM_DECK_CARD_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "ED40008", "하나의 커스텀 보관함에는 최대 50개의 카드만 저장할 수 있습니다"),

    DEFAULT_DECK_NOT_FOUND(HttpStatus.NOT_FOUND, "ED40401", "기본 보관함을 찾을 수 없습니다"),
    CARD_NOT_FOUND_IN_DECK(HttpStatus.NOT_FOUND, "ED40402", "보관함에 해당 카드가 존재하지 않습니다"),
    CUSTOM_DECK_NOT_FOUND(HttpStatus.NOT_FOUND, "ED40403", "커스텀 보관함을 찾을 수 없습니다"),
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
