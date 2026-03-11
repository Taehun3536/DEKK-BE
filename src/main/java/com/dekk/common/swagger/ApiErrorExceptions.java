package com.dekk.common.swagger;

import com.dekk.common.error.ErrorCode;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Swagger 문서에 여러 비즈니스 예외 사례를 자동으로 추가하기 위한 어노테이션입니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorExceptions {
    Class<? extends ErrorCode>[] value();
}
