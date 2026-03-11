package com.dekk.auth.domain.model;

import com.dekk.auth.domain.exception.AuthBusinessException;
import com.dekk.auth.domain.exception.AuthErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    private Long userId;
    private String token;

    private RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public static RefreshToken create(Long userId, String token) {
        if (userId == null || token == null || token.isBlank()) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_REFRESH_TOKEN);
        }
        return new RefreshToken(userId, token);
    }
}
