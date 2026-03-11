package com.dekk.crawl.domain.model.enums;

import com.dekk.crawl.domain.exception.CrawlBusinessException;
import com.dekk.crawl.domain.exception.CrawlErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CrawlBatchStatus {
    COLLECTING("수집 중", null, null),
    COLLECTED("수집 완료", COLLECTING, CrawlErrorCode.BATCH_NOT_COLLECTING),
    PROCESSING("정제 처리 중", COLLECTED, CrawlErrorCode.BATCH_NOT_COLLECTED),
    COMPLETED("처리 완료", PROCESSING, CrawlErrorCode.BATCH_NOT_PROCESSING);

    private final String description;
    private final CrawlBatchStatus requiredPrevious;
    private final CrawlErrorCode transitionErrorCode;

    public void validateTransitionFrom(CrawlBatchStatus current) {
        if (current != requiredPrevious) {
            throw new CrawlBusinessException(transitionErrorCode);
        }
    }
}
