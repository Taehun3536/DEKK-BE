package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Deck Command", description = "보관함 조작 API (Command)")
public interface DeckCommandApi {

    @Operation(summary = "기본 보관함에서 특정 카드 저장 취소 (Soft Delete)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SD20004: 보관함 내 카드 저장 취소 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "ED40401: 기본 보관함 미존재 / ED40402: 보관함 내 해당 카드 미존재",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> removeCardFromDefaultDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "삭제할 카드 ID", in = ParameterIn.PATH) Long cardId
    );

    @Operation(summary = "기본 보관함 카드 저장", description = "유저의 기본 보관함에 특정 카드를 저장합니다.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "SD20001: 보관함 카드 저장 성공"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "ED40401: 기본 보관함을 찾을 수 없습니다",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    ResponseEntity<ApiResponse<Void>> addCardToDefaultDeck(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @Parameter(description = "저장할 카드 ID", in = ParameterIn.PATH) Long cardId
    );
}
