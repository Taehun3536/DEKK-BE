package com.dekk.admin.domain.exception;

import com.dekk.common.error.BusinessException;
import com.dekk.common.error.ErrorCode;

public class AdminBusinessException extends BusinessException {
    public AdminBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
