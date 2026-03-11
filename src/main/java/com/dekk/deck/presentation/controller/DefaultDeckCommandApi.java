package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "기본 보관함 관리 API", description = "기본 보관함 및 기본 보관함 내 카드 관리 API")
public interface DefaultDeckCommandApi {

    @Operation(summary = "기본 보관함에서 특정 카드 저장 취소 (Soft Delete)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "SD20004: 보관함 내 카드 저장 취소 성공")
    @ApiErrorExceptions({DeckErrorCode.class})
    ResponseEntity<ApiResponse<Void>> removeCardFromDefaultDeck(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "삭제할 카드 ID", in = ParameterIn.PATH) Long cardId);
}
