package com.dekk.security.oauth2.repository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private final Map<String, OAuth2AuthorizationRequest> authRequests = new ConcurrentHashMap<>();
    private final Map<String, String> redirectUris = new ConcurrentHashMap<>();
    private final List<String> allowedOrigins;

    public InMemoryOAuth2AuthorizationRequestRepository(
            @Value("${app.cors.allowed-origins}") String allowedOrigins) {
        this.allowedOrigins = List.of(allowedOrigins.split(","));
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        String state = request.getParameter("state");
        return state != null ? authRequests.get(state) : null;
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String state = authorizationRequest.getState();
        authRequests.put(state, authorizationRequest);

        String redirectUri = request.getParameter("redirect_uri");
        if (redirectUri != null && !redirectUri.isBlank() && isAllowedOrigin(redirectUri)) {
            redirectUris.put(state, redirectUri);
        }
    }

    private boolean isAllowedOrigin(String uri) {
        return allowedOrigins.stream().anyMatch(uri::startsWith);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        String state = request.getParameter("state");
        if (state != null) {
            return authRequests.remove(state);
        }
        return null;
    }

    public String getRedirectUriAndRemove(String state) {
        if (state != null) {
            return redirectUris.remove(state);
        }
        return null;
    }
}
