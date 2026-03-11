package com.dekk.crawl.presentation.dto.request;

import com.dekk.card.domain.model.enums.Platform;
import com.dekk.crawl.application.command.CrawlBatchCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CrawlBatchCreateRequest(
        @Schema(description = "크롤링 대상 플랫폼", example = "MUSINSA") @NotNull(message = "플랫폼은 필수 값입니다.")
        Platform platform) {
    public CrawlBatchCreateCommand toCommand() {
        return new CrawlBatchCreateCommand(platform);
    }
}
