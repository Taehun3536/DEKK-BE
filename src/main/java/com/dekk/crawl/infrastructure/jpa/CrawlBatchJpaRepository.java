package com.dekk.crawl.infrastructure.jpa;

import com.dekk.crawl.domain.model.CrawlBatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlBatchJpaRepository extends JpaRepository<CrawlBatch, Long> {}
