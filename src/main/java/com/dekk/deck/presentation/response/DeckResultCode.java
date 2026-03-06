package com.dekk.deck.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DeckResultCode implements ResultCode {
    DEFAULT_DECK_CREATE_SUCCESS(HttpStatus.OK, "SD20001", "기본 보관함 생성 성공"),
    CARD_SAVE_SUCCESS(HttpStatus.OK, "SD20002", "보관함 카드 저장 성공"),
    DECK_CARD_LIST_SUCCESS(HttpStatus.OK, "SD20003", "보관함 카드 목록 조회 성공"),
    DECK_CARD_DELETED_SUCCESS(HttpStatus.OK, "SD20004", "보관함 내 카드 저장 취소 성공"),

    CUSTOM_DECK_CREATE_SUCCESS(HttpStatus.OK, "SD20005", "커스텀 보관함 생성 성공"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override public HttpStatus status() { return status; }
    @Override public String code() { return code; }
    @Override public String message() { return message; }
}
