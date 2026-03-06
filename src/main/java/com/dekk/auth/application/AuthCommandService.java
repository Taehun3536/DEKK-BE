package com.dekk.auth.application;

import com.dekk.auth.application.command.TokenRefreshCommand;
import com.dekk.auth.application.dto.result.TokenRefreshResult;
import com.dekk.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandService {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenRefreshResult refreshToken(TokenRefreshCommand command) {
        jwtTokenProvider.validateToken(command.refreshToken());

        Authentication authentication = jwtTokenProvider.getAuthentication((command.refreshToken()));

        String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return new TokenRefreshResult(newAccessToken, newRefreshToken);
    }
}
