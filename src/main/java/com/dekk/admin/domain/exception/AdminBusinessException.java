package com.dekk.admin.domain.exception;

import com.dekk.common.error.BusinessException;
import com.dekk.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class AdminBusinessException extends BusinessException {

    private final ErrorCode errorCode;

    public AdminBusinessException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
