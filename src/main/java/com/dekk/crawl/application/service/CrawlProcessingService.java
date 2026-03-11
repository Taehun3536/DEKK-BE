package com.dekk.crawl.application.service;

import com.dekk.crawl.domain.model.CrawlRawData;
import com.dekk.crawl.domain.model.enums.RawDataStatus;
import com.dekk.crawl.domain.repository.CrawlRawDataRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrawlProcessingService {

    private static final Logger log = LoggerFactory.getLogger("CRAWL");
    private static final int BATCH_SIZE = 50;

    private final CrawlRawDataRepository rawDataRepository;
    private final CrawlRawDataProcessor processor;

    public void processNextBatch() {
        List<CrawlRawData> pendingList = rawDataRepository.findByStatusWithLimit(RawDataStatus.PENDING, BATCH_SIZE);

        if (pendingList.isEmpty()) {
            log.info("처리할 PENDING raw data가 없습니다");
            return;
        }

        log.info("PENDING raw data {}건 처리 시작", pendingList.size());

        for (CrawlRawData rawData : pendingList) {
            processor.process(rawData.getId());
        }

        log.info("raw data 처리 완료");
    }
}
