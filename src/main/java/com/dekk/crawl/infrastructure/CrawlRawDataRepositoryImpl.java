package com.dekk.crawl.infrastructure;

import com.dekk.crawl.domain.model.CrawlRawData;
import com.dekk.crawl.domain.model.enums.RawDataStatus;
import com.dekk.crawl.domain.repository.CrawlRawDataRepository;
import com.dekk.crawl.infrastructure.jpa.CrawlRawDataJpaRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CrawlRawDataRepositoryImpl implements CrawlRawDataRepository {
    private final CrawlRawDataJpaRepository crawlRawDataJpaRepository;

    @Override
    public CrawlRawData save(CrawlRawData rawData) {
        return crawlRawDataJpaRepository.save(rawData);
    }

    @Override
    public Optional<CrawlRawData> findById(Long id) {
        return crawlRawDataJpaRepository.findById(id);
    }

    @Override
    public List<CrawlRawData> findByStatusWithLimit(RawDataStatus status, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return crawlRawDataJpaRepository.findByStatusOrderByIdAsc(status, pageable);
    }

    @Override
    public long countByBatchIdAndStatusNot(Long batchId, RawDataStatus status) {
        return crawlRawDataJpaRepository.countByBatchIdAndStatusNot(batchId, status);
    }
}
