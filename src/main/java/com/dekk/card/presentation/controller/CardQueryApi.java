package com.dekk.card.presentation.controller;

import com.dekk.card.presentation.dto.response.GuestCardResponse;
import com.dekk.card.presentation.dto.response.MemberCardResponse;
import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "카드 조회 API", description = "카드 조회 관련 API")
public interface CardQueryApi {

    @Operation(
            summary = "카드 목록 조회",
            description = "카드 목록을 페이징하여 조회합니다. "
                    + "인증 토큰이 없으면 카드 기본 정보만, 인증 토큰이 있으면 상품 정보를 포함하여 반환합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "카드 목록 조회 성공",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(oneOf = {GuestCardResponse.class, MemberCardResponse.class}),
                            examples = {
                                    @ExampleObject(
                                            name = "비회원 응답 (토큰 없음)",
                                            summary = "인증 토큰이 없을 때",
                                            value = """
                                                    {
                                                      "code": "SC200001",
                                                      "message": "비회원 카드 목록 조회 성공",
                                                      "data": {
                                                        "content": [
                                                          {
                                                            "cardId": 1,
                                                            "cardImageUrl": "https://example.com/card.jpg",
                                                            "height": 175,
                                                            "weight": 65,
                                                            "tags": ["캐주얼", "스트릿"]
                                                          }
                                                        ],
                                                        "currentPage": 0,
                                                        "size": 10,
                                                        "totalElements": 1,
                                                        "totalPages": 1,
                                                        "hasNext": false
                                                      }
                                                    }"""
                                    ),
                                    @ExampleObject(
                                            name = "회원 응답 (토큰 있음)",
                                            summary = "인증 토큰이 있을 때",
                                            value = """
                                                    {
                                                      "code": "SC200002",
                                                      "message": "회원 카드 목록 조회 성공",
                                                      "data": {
                                                        "content": [
                                                          {
                                                            "cardId": 1,
                                                            "cardImageUrl": "https://example.com/card.jpg",
                                                            "height": 175,
                                                            "weight": 65,
                                                            "tags": ["캐주얼", "스트릿"],
                                                            "products": [
                                                              {
                                                                "productId": 10,
                                                                "brand": "Nike",
                                                                "name": "에어맥스 90",
                                                                "productImageUrl": "https://example.com/product.jpg",
                                                                "productUrl": "https://example.com/product"
                                                              }
                                                            ]
                                                          }
                                                        ],
                                                        "currentPage": 0,
                                                        "size": 10,
                                                        "totalElements": 1,
                                                        "totalPages": 1,
                                                        "hasNext": false
                                                      }
                                                    }"""
                                    )
                            }
                    )
            )
    })
    ResponseEntity<ApiResponse<PageResponse<?>>> getCards(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    );
}
