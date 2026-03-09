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
    CUSTOM_DECK_UPDATE_SUCCESS(HttpStatus.OK, "SD20006", "커스텀 보관함 이름 수정 성공"),
    CUSTOM_DECK_DELETE_SUCCESS(HttpStatus.OK, "SD20007", "커스텀 보관함 삭제 성공"),
    CUSTOM_DECK_LIST_SUCCESS(HttpStatus.OK, "SD20008", "커스텀 보관함 목록 조회 성공"),
    CUSTOM_DECK_CARD_SAVE_SUCCESS(HttpStatus.OK, "SD20009", "커스텀 보관함 카드 저장 성공"),
    CUSTOM_DECK_CARD_DELETE_SUCCESS(HttpStatus.OK, "SD20010", "커스텀 보관함 내 카드 삭제 성공"),
    CUSTOM_DECK_CARD_LIST_SUCCESS(HttpStatus.OK, "SD20011", "커스텀 보관함 카드 목록 조회 성공"),
    DECK_LIST_SUCCESS(HttpStatus.OK, "SD20012", "보관함 통합 목록 조회 성공"),
    ;

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
