package com.dekk.auth.application.dto.result;

public record TokenRefreshResult(
        String accessToken,
        String refreshToken
) {
}
