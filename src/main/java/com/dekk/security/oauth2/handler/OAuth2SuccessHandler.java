package com.dekk.security.oauth2.handler;

import com.dekk.auth.jwt.JwtTokenProvider;
import com.dekk.auth.presentation.util.CookieUtil;
import com.dekk.security.oauth2.repository.InMemoryOAuth2AuthorizationRequestRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final InMemoryOAuth2AuthorizationRequestRepository inMemoryRepository;
    private final String defaultRedirectUri;

    private final int accessTokenMaxAge;
    private final int refreshTokenMaxAge;

    public OAuth2SuccessHandler(
            JwtTokenProvider jwtTokenProvider,
            InMemoryOAuth2AuthorizationRequestRepository inMemoryRepository,
            @Value("${app.oauth2.redirect-uri}") String defaultRedirectUri,
            @Value("${jwt.access-token-validity-in-seconds}") int accessTokenMaxAge,
            @Value("${jwt.refresh-token-validity-in-seconds}") int refreshTokenMaxAge) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.inMemoryRepository = inMemoryRepository;
        this.defaultRedirectUri = defaultRedirectUri;
        this.accessTokenMaxAge = accessTokenMaxAge;
        this.refreshTokenMaxAge = refreshTokenMaxAge;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 authentication successful. Generating JWT token...");

        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        log.info("Generated Access/Refresh Token (Hidden in Cookie)");

        CookieUtil.addCookie(response, CookieUtil.ACCESS_TOKEN_NAME, accessToken, accessTokenMaxAge);
        CookieUtil.addCookie(response, CookieUtil.REFRESH_TOKEN_NAME, refreshToken, refreshTokenMaxAge);

        String state = request.getParameter("state");
        String requestedRedirectUri = inMemoryRepository.getRedirectUriAndRemove(state);
        String targetUrl = StringUtils.hasText(requestedRedirectUri) ? requestedRedirectUri : defaultRedirectUri;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
