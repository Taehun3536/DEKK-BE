package com.dekk.auth.jwt;

import com.dekk.auth.domain.exception.AuthBusinessException;
import com.dekk.auth.domain.exception.AuthErrorCode;
import com.dekk.security.oauth2.CustomUserDetails;
import com.dekk.user.domain.model.enums.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String TOKEN_TYPE_KEY = "type";
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_STATUS = "status";
    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    private final Key key;
    private final long accessTokenValidityTime;
    private final long refreshTokenValidityTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityTime = refreshTokenValidityInSeconds * 1000;
    }

    public String createAccessToken(Authentication authentication) {
        return createToken(authentication, accessTokenValidityTime, ACCESS_TOKEN_TYPE);
    }

    public String createRefreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenValidityTime, REFRESH_TOKEN_TYPE);
    }

    private String createToken(Authentication authentication, long tokenValidTime, String tokenType) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Long userId = userDetails.getId();
        UserStatus status = userDetails.getStatus();

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidTime);

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, authorities)
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_STATUS, status)
                .claim(TOKEN_TYPE_KEY, tokenType)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(validity)
                .compact();
    }

    public boolean isAccessToken(String token) {
        String type = getClaims(token).get(TOKEN_TYPE_KEY, String.class);
        return ACCESS_TOKEN_TYPE.equals(type);
    }

    public boolean isRefreshToken(String token) {
        String type = getClaims(token).get(TOKEN_TYPE_KEY, String.class);
        return REFRESH_TOKEN_TYPE.equals(type);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String role = claims.get(AUTHORITIES_KEY).toString();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(role.split(",")).map(SimpleGrantedAuthority::new).toList();

        String email = claims.getSubject();
        Long userId = ((Number) claims.get(CLAIM_USER_ID)).longValue();
        String status = claims.get(CLAIM_STATUS, String.class);

        CustomUserDetails principal = new CustomUserDetails(userId, email, role, UserStatus.valueOf(status));

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new AuthBusinessException(AuthErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new AuthBusinessException(AuthErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new AuthBusinessException(AuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new AuthBusinessException(AuthErrorCode.EMPTY_CLAIMS);
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
