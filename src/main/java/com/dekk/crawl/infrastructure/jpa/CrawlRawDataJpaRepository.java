package com.dekk.crawl.infrastructure.jpa;

import com.dekk.crawl.domain.model.CrawlRawData;
import com.dekk.crawl.domain.model.enums.RawDataStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlRawDataJpaRepository extends JpaRepository<CrawlRawData, Long> {
    List<CrawlRawData> findByStatusOrderByIdAsc(RawDataStatus status, Pageable pageable);

    long countByBatchIdAndStatusNot(Long batchId, RawDataStatus status);
}
