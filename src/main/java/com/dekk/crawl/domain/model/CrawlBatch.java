package com.dekk.crawl.domain.model;

import static com.dekk.crawl.domain.model.enums.CrawlBatchStatus.COLLECTED;
import static com.dekk.crawl.domain.model.enums.CrawlBatchStatus.COMPLETED;
import static com.dekk.crawl.domain.model.enums.CrawlBatchStatus.PROCESSING;

import com.dekk.card.domain.model.enums.Platform;
import com.dekk.common.entity.BaseTimeEntity;
import com.dekk.crawl.domain.exception.CrawlBusinessException;
import com.dekk.crawl.domain.exception.CrawlErrorCode;
import com.dekk.crawl.domain.model.enums.CrawlBatchStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "crawl_batches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlBatch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CrawlBatchStatus status;

    private CrawlBatch(Platform platform) {
        this.platform = platform;
        this.status = CrawlBatchStatus.COLLECTING;
    }

    public static CrawlBatch create(Platform platform) {
        if (platform == null) {
            throw new CrawlBusinessException(CrawlErrorCode.PLATFORM_IS_REQUIRED);
        }
        return new CrawlBatch(platform);
    }

    public void markAsCollected() {
        COLLECTED.validateTransitionFrom(this.status);
        this.status = COLLECTED;
    }

    public void markAsProcessing() {
        PROCESSING.validateTransitionFrom(this.status);
        this.status = PROCESSING;
    }

    public void markAsCompleted() {
        COMPLETED.validateTransitionFrom(this.status);
        this.status = COMPLETED;
    }
}
