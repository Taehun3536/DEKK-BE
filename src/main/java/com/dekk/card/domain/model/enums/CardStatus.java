package com.dekk.card.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CardStatus {
    PENDING("검수 중"),
    APPROVED("승인됨"),
    REJECTED("반려됨"),
    DELETE_REQUESTED("삭제 요청 됨");

    private final String description;

    public boolean canChangeStatus() {
        return this != DELETE_REQUESTED;
    }
}
