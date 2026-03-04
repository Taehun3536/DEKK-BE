package com.dekk.crawl.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CrawlResultCode implements ResultCode {
    BATCH_COLLECTION_COMPLETED(HttpStatus.OK, "SCR20001", "수집이 완료되었습니다"),

    BATCH_CREATED(HttpStatus.CREATED, "SCR20101", "배치가 생성되었습니다"),
    RAW_DATA_RECEIVED(HttpStatus.CREATED, "SCR20102", "원본 데이터가 수신되었습니다"),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public HttpStatus status() { return status; }

    @Override
    public String code() { return code; }

    @Override
    public String message() { return message; }
}
