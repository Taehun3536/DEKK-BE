package com.dekk.security.oauth2.handler;

import com.dekk.auth.domain.model.RefreshToken;
import com.dekk.auth.domain.repository.RefreshTokenRepository;
import com.dekk.auth.jwt.JwtTokenProvider;
import com.dekk.auth.presentation.util.CookieUtil;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.domain.model.enums.UserStatus;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieUtil cookieUtil;

    private final String defaultRedirectUri;
    private final String joinPageUri;

    private final int accessTokenMaxAge;
    private final int refreshTokenMaxAge;

    public OAuth2SuccessHandler(
            JwtTokenProvider jwtTokenProvider,
            RefreshTokenRepository refreshTokenRepository,
            CookieUtil cookieUtil,
            @Value("${app.oauth2.redirect-uri}") String defaultRedirectUri,
            @Value("${app.oauth2.join-page}") String joinPageUri,
            @Value("${jwt.access-token-validity-in-seconds}") int accessTokenMaxAge,
            @Value("${jwt.refresh-token-validity-in-seconds}") int refreshTokenMaxAge) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.cookieUtil = cookieUtil;
        this.defaultRedirectUri = defaultRedirectUri;
        this.joinPageUri = joinPageUri;
        this.accessTokenMaxAge = accessTokenMaxAge;
        this.refreshTokenMaxAge = refreshTokenMaxAge;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        cookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_NAME, accessToken, accessTokenMaxAge);
        cookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_NAME, refreshToken, refreshTokenMaxAge);

        String targetUrl = defaultRedirectUri;

        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            refreshTokenRepository.save(RefreshToken.create(userDetails.getId(), refreshToken));

            if (userDetails.getStatus() == UserStatus.PENDING) {
                targetUrl = joinPageUri;
            }
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
