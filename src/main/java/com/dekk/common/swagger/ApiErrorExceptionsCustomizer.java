package com.dekk.common.swagger;

import com.dekk.common.error.ErrorCode;
import com.dekk.common.error.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class ApiErrorExceptionsCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiErrorExceptions annotation =
                AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getMethod(), ApiErrorExceptions.class);

        if (annotation != null) {
            ApiResponses responses = operation.getResponses();

            for (Class<? extends ErrorCode> errorCodeClass : annotation.value()) {
                ErrorCode[] errorCodes = errorCodeClass.getEnumConstants();

                if (errorCodes == null) continue;

                for (ErrorCode errorCode : errorCodes) {
                    String statusCode = String.valueOf(errorCode.status().value());

                    Example example = new Example();
                    example.setValue(ErrorResponse.from(errorCode));
                    example.setDescription(errorCode.message());

                    ApiResponse response =
                            responses.computeIfAbsent(statusCode, k -> new ApiResponse().description("에러 응답"));

                    if (response.getContent() == null) {
                        response.setContent(new Content().addMediaType("application/json", new MediaType()));
                    }

                    response.getContent().get("application/json").addExamples(errorCode.name(), example);
                }
            }
        }
        return operation;
    }
}
