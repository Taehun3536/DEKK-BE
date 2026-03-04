package com.dekk.crawl.application.result;

import com.dekk.crawl.domain.model.CrawlRawData;

public record CrawlRawDataResult(
        Long rawDataId,
        String status
) {
    public static CrawlRawDataResult from(CrawlRawData rawData) {
        return new CrawlRawDataResult(
                rawData.getId(),
                rawData.getStatus().name()
        );
    }
}
