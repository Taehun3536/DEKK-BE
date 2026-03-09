package com.dekk.security.oauth2.handler;

import com.dekk.security.oauth2.dto.ErrorQueryParam;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String redirectUri;
    private final OAuth2ErrorMapper errorMapper;

    public OAuth2FailureHandler(
            @Value("${app.oauth2.redirect-uri}") String redirectUri,
            OAuth2ErrorMapper errorMapper
    ) {
        this.redirectUri = redirectUri;
        this.errorMapper = errorMapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("OAuth2 authentication failed: {}", exception.getMessage());

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(redirectUri);

        ErrorQueryParam queryParam = errorMapper.mapError(exception);

        builder.queryParam("error", queryParam.error());
        if (queryParam.provider() != null && !queryParam.provider().isBlank()) {
            builder.queryParam("provider", queryParam.provider());
        }

        String targetUrl = builder.build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
