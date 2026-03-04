package com.dekk.crawl.presentation.dto.request;

import com.dekk.crawl.application.command.CrawlRawDataCreateCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CrawlRawDataCreateRequest(
        @Schema(description = "크롤링 원본 데이터 (JSON 문자열)", example = "[{\"id\": \"12345\", ...}]")
        @NotBlank(message = "원본 데이터는 필수 값입니다.")
        String rawData
) {
    public CrawlRawDataCreateCommand toCommand(Long batchId) {
        return new CrawlRawDataCreateCommand(batchId, rawData);
    }
}
