package com.dekk.auth.presentation.controller;

import com.dekk.auth.presentation.request.TokenRefreshRequest;
import com.dekk.auth.presentation.response.TokenResponse;
import com.dekk.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증 API", description = "토큰 재발급 및 로그아웃 API")
public interface AuthApi {

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SA20001)"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 토큰(EA40101) / 만료된 토큰(EA40102) / 지원되지 않는 토큰(EA40103) / 빈 클레임(EA40104)",
                    content = @Content(schema = @Schema(implementation = com.dekk.common.error.ErrorResponse.class))
            )
    })
    ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    );

    @Operation(summary = "로그아웃", description = "로그아웃을 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공 (SA20002)")
    })
    ResponseEntity<ApiResponse<Void>> logout();
}
