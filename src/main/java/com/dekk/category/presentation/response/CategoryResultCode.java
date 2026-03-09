package com.dekk.category.presentation.response;

import com.dekk.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryResultCode implements ResultCode {
    CATEGORY_TREE_FETCHED(HttpStatus.OK, "SCT20001", "카테고리 트리 조회 성공"),

    CATEGORY_CREATED(HttpStatus.CREATED, "SCT20101", "카테고리 생성 성공")
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
