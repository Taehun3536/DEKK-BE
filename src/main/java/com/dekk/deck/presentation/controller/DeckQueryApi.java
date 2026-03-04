package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.deck.application.dto.result.MyDeckCardResult;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Deck Query", description = "보관함 조회 API")
public interface DeckQueryApi {

    @Operation(summary = "기본 보관함 카드 목록 페이징 조회")
    ResponseEntity<ApiResponse<PageResponse<MyDeckCardResult>>> getMyDefaultDeckCards(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        Pageable pageable
    );
}
