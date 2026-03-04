package com.dekk.crawl.application.result;

import com.dekk.card.domain.model.enums.Platform;
import com.dekk.crawl.domain.model.CrawlBatch;

public record CrawlBatchResult(
        Long batchId,
        Platform platform,
        String status
) {
    public static CrawlBatchResult from(CrawlBatch batch) {
        return new CrawlBatchResult(
                batch.getId(),
                batch.getPlatform(),
                batch.getStatus().name()
        );
    }
}
