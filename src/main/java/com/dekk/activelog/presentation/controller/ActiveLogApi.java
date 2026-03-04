package com.dekk.activelog.presentation.controller;

import com.dekk.activelog.presentation.request.SwipeRequest;
import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "스와이프 액션 API", description = "카드에 대한 좋아요/싫어요 평가 기록 API")
public interface ActiveLogApi {

    @Operation(summary = "카드 스와이프 평가 저장", description = "특정 카드에 대한 사용자의 액션을 기록합니다. (비회원은 수집 안함)")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SAL20001)"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터 또는 SwipeType 오류 (EAL40001)"),
    })
    ResponseEntity<ApiResponse<Void>> swipeCard(
        @Parameter(description = "대상 카드 ID", in = ParameterIn.PATH) Long cardId,
        @RequestBody(description = "스와이프 요청 정보(LIKE/DISLIKE)") SwipeRequest request,
        @Parameter(hidden = true) CustomUserDetails userDetails
    );
}
