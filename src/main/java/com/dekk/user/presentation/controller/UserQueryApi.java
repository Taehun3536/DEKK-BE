package com.dekk.user.presentation.controller;

import com.dekk.common.response.ApiResponse;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.presentation.response.UserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "사용자 정보 조회 API", description = "사용자 정보 조회 API")
public interface UserQueryApi {

    @Operation(summary = "내 정보 조회", description = "현재 로그인된 사용자의 상세 프로필 정보를 조회합니다.")
    ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails);
}
