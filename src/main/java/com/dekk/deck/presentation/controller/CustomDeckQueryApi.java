package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.swagger.ApiErrorExceptions;
import com.dekk.deck.application.dto.result.CustomDeckResult;
import com.dekk.deck.application.dto.result.MyDeckCardResult;
import com.dekk.deck.domain.exception.DeckErrorCode;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "커스텀 보관함 조회 API", description = "커스텀 보관함 목록 및 상태 조회 API")
public interface CustomDeckQueryApi {

    @Operation(summary = "내 커스텀 보관함 목록 조회", description = "사용자가 생성한 커스텀 보관함 목록과 최신 카드 썸네일(imageUrl) 1장을 최신순으로 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20008)")
    @ApiErrorExceptions({DeckErrorCode.class})
    ResponseEntity<ApiResponse<List<CustomDeckResult>>> getMyCustomDecks(
            @Parameter(hidden = true) CustomUserDetails userDetails);

    @Operation(summary = "커스텀 보관함 내부 카드 목록 조회", description = "특정 커스텀 보관함에 담긴 카드 목록을 최신순으로 전체 조회합니다. (최대 50장)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SD20011)")
    @ApiErrorExceptions({DeckErrorCode.class})
    ResponseEntity<ApiResponse<List<MyDeckCardResult>>> getCustomDeckCards(
            @Parameter(hidden = true) CustomUserDetails userDetails,
            @Parameter(description = "조회할 커스텀 보관함 ID", in = ParameterIn.PATH) Long customDeckId);
}
