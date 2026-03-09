package com.dekk.category.domain.exception;

import com.dekk.common.error.BusinessException;
import com.dekk.common.error.ErrorCode;

public class CategoryBusinessException extends BusinessException {
    public CategoryBusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}
