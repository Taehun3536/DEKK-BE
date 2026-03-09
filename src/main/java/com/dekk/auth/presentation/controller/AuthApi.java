package com.dekk.auth.presentation.controller;

import com.dekk.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

@Tag(name = "인증 API", description = "토큰 재발급 및 로그아웃 API (HttpOnly 쿠키 기반)")
public interface AuthApi {

    @Operation(summary = "토큰 재발급", description = "쿠키에 담긴 리프레시 토큰을 검증하여 새로운 액세스/리프레시 토큰 쿠키를 발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SA20001)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 토큰(EA40101)")
    })
    ResponseEntity<ApiResponse<Void>> refreshToken(
            @Parameter(hidden = true) @CookieValue(value = "refresh_token") String refreshToken,
            @Parameter(hidden = true) HttpServletResponse response
    );

    @Operation(summary = "로그아웃", description = "발급된 인증 쿠키를 모두 삭제(만료) 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공 (SA20002)")
    })
    ResponseEntity<ApiResponse<Void>> logout(
            @Parameter(hidden = true) HttpServletResponse response
    );
}
