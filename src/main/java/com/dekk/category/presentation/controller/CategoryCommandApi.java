package com.dekk.category.presentation.controller;

import com.dekk.category.presentation.request.CreateCategoryRequest;
import com.dekk.category.presentation.response.CreateCategoryResponse;
import com.dekk.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "관리자 카테고리 관리 API", description = "관리자가 카테고리를 생성하고 조회하는 API")
public interface CategoryCommandApi {

    @Operation(summary = "상위 카테고리 생성", description = "새로운 상위 카테고리를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "카테고리 생성 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20101",
                                      "message": "카테고리 생성 성공",
                                      "data": {
                                        "categoryId": 1
                                      }
                                    }""")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "유효하지 않은 요청",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40001",
                                      "message": "카테고리 이름은 필수값입니다"
                                    }""")
                    )
            )
    })
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createParentCategory(
            @Valid CreateCategoryRequest request
    );

    @Operation(summary = "하위 카테고리 생성", description = "상위 카테고리에 속하는 하위 카테고리를 생성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "카테고리 생성 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20101",
                                      "message": "카테고리 생성 성공",
                                      "data": {
                                        "categoryId": 2
                                      }
                                    }""")
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "상위 카테고리를 찾을 수 없음",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40401",
                                      "message": "카테고리를 찾을 수 없습니다"
                                    }""")
                    )
            )
    })
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createChildCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long parentId,
            @Valid CreateCategoryRequest request
    );

}
