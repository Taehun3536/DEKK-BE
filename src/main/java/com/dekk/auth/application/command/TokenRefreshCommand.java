package com.dekk.auth.application.command;

public record TokenRefreshCommand(
        String refreshToken
) {
}
