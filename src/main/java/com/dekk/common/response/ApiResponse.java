package com.dekk.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public record ApiResponse<T>(String code, String message, T data) {
    public static <T> ApiResponse<T> from(ResultCode resultCode) {
        return ApiResponse.of(resultCode, null);
    }

    public static <T> ApiResponse<T> of(ResultCode resultCode, T data) {
        return new ApiResponse<>(resultCode.code(), resultCode.message(), data);
    }
}
