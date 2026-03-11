package com.dekk.category.presentation.controller;

import com.dekk.category.presentation.request.CreateCategoryRequest;
import com.dekk.category.presentation.request.UpdateCategoryNameRequest;
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

@Tag(name = "관리자 카테고리 관리 API", description = "관리자가 카테고리를 생성, 수정, 삭제하는 API")
public interface CategoryCommandApi {

    @Operation(summary = "상위 카테고리 생성", description = "새로운 상위 카테고리를 생성합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "카테고리 생성 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20101",
                                      "message": "카테고리 생성에 성공했습니다.",
                                      "data": {
                                        "categoryId": 1
                                      }
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "유효하지 않은 요청",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40001",
                                      "message": "카테고리 이름은 필수값입니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createParentCategory(@Valid CreateCategoryRequest request);

    @Operation(summary = "하위 카테고리 생성", description = "상위 카테고리에 속하는 하위 카테고리를 생성합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "201",
                        description = "카테고리 생성에 성공했습니다.",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20101",
                                      "message": "카테고리 생성에 성공했습니다.",
                                      "data": {
                                        "categoryId": 2
                                      }
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "상위 카테고리를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40401",
                                      "message": "카테고리를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<CreateCategoryResponse>> createChildCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long parentId,
            @Valid CreateCategoryRequest request);

    @Operation(summary = "카테고리 이름 수정", description = "상위 또는 하위 카테고리의 이름을 수정합니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카테고리명 수정 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20002",
                                      "message": "카테고리명 수정에 성공했습니다."
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카테고리를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40401",
                                      "message": "카테고리를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<Void>> updateCategoryName(
            @Parameter(description = "카테고리 ID", in = ParameterIn.PATH) Long categoryId,
            @Valid UpdateCategoryNameRequest request);

    @Operation(summary = "상위 카테고리 삭제", description = "상위 카테고리를 삭제합니다. 하위 카테고리와 관련 카드 매핑도 함께 삭제됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카테고리 삭제 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20003",
                                      "message": "카테고리 삭제에 성공했습니다."
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카테고리를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40401",
                                      "message": "카테고리를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<Void>> deleteParentCategory(
            @Parameter(description = "상위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);

    @Operation(summary = "하위 카테고리 삭제", description = "하위 카테고리를 삭제합니다. 관련 카드 매핑도 함께 삭제됩니다.")
    @ApiResponses(
            value = {
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "200",
                        description = "카테고리 삭제 성공",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20003",
                                      "message": "카테고리 삭제에 성공했습니다."
                                    }"""))),
                @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "404",
                        description = "카테고리를 찾을 수 없음",
                        content =
                                @Content(
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = @ExampleObject(value = """
                                    {
                                      "code": "ECT40401",
                                      "message": "카테고리를 찾을 수 없습니다"
                                    }""")))
            })
    ResponseEntity<ApiResponse<Void>> deleteChildCategory(
            @Parameter(description = "하위 카테고리 ID", in = ParameterIn.PATH) Long categoryId);
}
