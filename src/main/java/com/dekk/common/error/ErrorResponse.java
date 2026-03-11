package com.dekk.common.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record ErrorResponse(String code, String message, List<String> errors) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.of(errorCode, null);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<String> errors) {
        return new ErrorResponse(errorCode.code(), errorCode.message(), errors);
    }
}
