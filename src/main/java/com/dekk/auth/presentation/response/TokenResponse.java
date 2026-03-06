package com.dekk.auth.presentation.response;

import com.dekk.auth.application.dto.result.TokenRefreshResult;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
    public static TokenResponse from(TokenRefreshResult result){
        return new TokenResponse(result.accessToken(), result.refreshToken());
    }
}
