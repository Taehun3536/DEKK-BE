package com.dekk.crawl.domain.model.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RawDataStatus {
    PENDING("처리 대기"),
    PROCESSING("처리 중"),
    COMPLETED("처리 완료"),
    FAILED("실패");

    private final String description;
}
