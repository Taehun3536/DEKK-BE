package com.dekk.crawl.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.crawl.application.result.CrawlBatchResult;
import com.dekk.crawl.application.result.CrawlRawDataResult;
import com.dekk.crawl.application.service.CrawlCommandService;
import com.dekk.crawl.presentation.dto.request.CrawlBatchCreateRequest;
import com.dekk.crawl.presentation.dto.request.CrawlRawDataCreateRequest;
import com.dekk.crawl.presentation.dto.response.CrawlBatchCreateResponse;
import com.dekk.crawl.presentation.dto.response.CrawlRawDataCreateResponse;
import com.dekk.crawl.presentation.response.CrawlResultCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/i/v1/crawl")
@RequiredArgsConstructor
public class CrawlController implements CrawlApi {

    private final CrawlCommandService commandService;

    @Override
    @PostMapping("/batches")
    public ResponseEntity<ApiResponse<CrawlBatchCreateResponse>> createBatch(
            @Valid @RequestBody CrawlBatchCreateRequest request) {
        CrawlBatchResult result = commandService.createBatch(request.toCommand());
        return ResponseEntity.status(CrawlResultCode.BATCH_CREATED.status())
                .body(ApiResponse.of(CrawlResultCode.BATCH_CREATED, CrawlBatchCreateResponse.from(result)));
    }

    @Override
    @PostMapping("/batches/{batchId}/raw-data")
    public ResponseEntity<ApiResponse<CrawlRawDataCreateResponse>> addRawData(
            @PathVariable Long batchId, @Valid @RequestBody CrawlRawDataCreateRequest request) {
        CrawlRawDataResult result = commandService.addRawData(request.toCommand(batchId));
        return ResponseEntity.status(CrawlResultCode.RAW_DATA_RECEIVED.status())
                .body(ApiResponse.of(CrawlResultCode.RAW_DATA_RECEIVED, CrawlRawDataCreateResponse.from(result)));
    }

    @Override
    @PostMapping("/batches/{batchId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeBatch(@PathVariable Long batchId) {
        commandService.completeBatch(batchId);
        return ResponseEntity.ok(ApiResponse.from(CrawlResultCode.BATCH_COLLECTION_COMPLETED));
    }
}
