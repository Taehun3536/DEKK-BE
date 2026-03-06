package com.dekk.auth.presentation.controller;

import com.dekk.auth.application.AuthCommandService;
import com.dekk.auth.application.dto.result.TokenRefreshResult;
import com.dekk.auth.presentation.request.TokenRefreshRequest;
import com.dekk.auth.presentation.response.AuthResultCode;
import com.dekk.auth.presentation.response.TokenResponse;
import com.dekk.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/w/v1/auth")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthCommandService authCommandService;

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        TokenRefreshResult result = authCommandService.refreshToken(request.toCommand());

        return ResponseEntity.ok(ApiResponse.of(AuthResultCode.TOKEN_REFRESH_SUCCESS, TokenResponse.from(result)));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.from(AuthResultCode.LOGOUT_SUCCESS));
    }
}
