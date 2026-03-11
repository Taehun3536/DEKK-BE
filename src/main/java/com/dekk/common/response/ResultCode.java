package com.dekk.common.response;

import org.springframework.http.HttpStatus;

public interface ResultCode {
    HttpStatus status();

    String code();

    String message();
}
