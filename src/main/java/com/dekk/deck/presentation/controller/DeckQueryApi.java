package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.deck.application.dto.result.DeckResult;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "통합 보관함 조회 API", description = "메인 홈 화면용 보관함 목록 통합 조회 API")
public interface DeckQueryApi {

    @Operation(summary = "내 보관함 통합 목록 조회", description = "기본 보관함과 커스텀 보관함을 통합 조회하며, 각 보관함의 최신 카드 썸네일(최대 3장)을 포함합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "SD20012: 보관함 통합 목록 조회 성공"
        )
    })
    ResponseEntity<ApiResponse<List<DeckResult>>> getDecks(
        @Parameter(hidden = true) CustomUserDetails userDetails
    );
}
