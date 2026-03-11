package com.dekk.card.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CardResultCode implements ResultCode {
    GUEST_CARD_LIST_SUCCESS(HttpStatus.OK, "SC200001", "비회원 카드 목록 조회 성공"),
    MEMBER_CARD_LIST_SUCCESS(HttpStatus.OK, "SC200002", "회원 카드 목록 조회 성공"),
    CARD_APPROVE_SUCCESS(HttpStatus.OK, "SC200003", "카드 승인 성공"),
    CARD_REJECT_SUCCESS(HttpStatus.OK, "SC200004", "카드 반려 성공");

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
