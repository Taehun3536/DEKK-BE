package com.dekk.auth.presentation.request;

import com.dekk.auth.application.command.TokenRefreshCommand;
import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
        @NotBlank(message = "Refresh Token은 필수입니다.")
        String refreshToken
) {
    public TokenRefreshCommand toCommand() {
        return new TokenRefreshCommand(refreshToken);
    }
}
