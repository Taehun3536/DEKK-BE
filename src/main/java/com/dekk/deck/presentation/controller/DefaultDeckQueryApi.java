package com.dekk.deck.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.common.response.PageResponse;
import com.dekk.deck.application.dto.result.MyDeckCardResult;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

@Tag(name = "기본 보관함 조회 API", description = "기본 보관함 카드 목록 조회 API")
public interface DefaultDeckQueryApi {

    @Operation(summary = "기본 보관함 카드 목록 페이징 조회", description = "기본 보관함의 카드 목록을 페이징하여 조회합니다. (기본 정렬: 최신순)")
    ResponseEntity<ApiResponse<PageResponse<MyDeckCardResult>>> getMyDefaultDeckCards(
        @Parameter(hidden = true) CustomUserDetails userDetails,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    );
}
