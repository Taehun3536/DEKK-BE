package com.dekk.category.presentation.controller;

import com.dekk.category.presentation.response.CategoryListResponse;
import com.dekk.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "관리자 카테고리 조회 API", description = "관리자가 카테고리를 조회하는 API")
public interface CategoryQueryApi {

    @Operation(summary = "전체 카테고리 트리 조회", description = "모든 상위 카테고리와 하위 카테고리를 트리 구조로 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카테고리 트리 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = """
                                    {
                                      "code": "SCT20010",
                                      "message": "카테고리 트리 조회 성공",
                                      "data": [
                                        {
                                          "categoryId": 1,
                                          "name": "계절",
                                          "children": [
                                            {
                                              "categoryId": 2,
                                              "name": "봄"
                                            },
                                            {
                                              "categoryId": 3,
                                              "name": "여름"
                                            }
                                          ]
                                        }
                                      ]
                                    }""")
                    )
            )
    })
    ResponseEntity<ApiResponse<List<CategoryListResponse>>> getCategoryTree();
}
