package com.dekk.auth.infrastructure.redis;

import com.dekk.auth.domain.model.RefreshToken;
import com.dekk.auth.domain.repository.RefreshTokenRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX = "RT:";

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenTtlSeconds;

    @Override
    public void save(RefreshToken refreshToken) {
        String key = PREFIX + refreshToken.getUserId();
        try {
            redisTemplate.opsForValue().set(key, refreshToken.getToken(), refreshTokenTtlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error(
                    "[Redis Fail-Safe] Refresh Token 저장 실패 - UserId: {}, Reason: {}",
                    refreshToken.getUserId(),
                    e.getMessage());
        }
    }

    @Override
    public Optional<RefreshToken> findByUserId(Long userId) {
        String key = PREFIX + userId;
        try {
            String token = redisTemplate.opsForValue().get(key);
            if (token == null) {
                return Optional.empty();
            }
            return Optional.of(RefreshToken.create(userId, token));
        } catch (Exception e) {
            log.error("[Redis Fail-Safe] Refresh Token 조회 실패 - UserId: {}, Reason: {}", userId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void deleteByUserId(Long userId) {
        String key = PREFIX + userId;
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("[Redis Fail-Safe] Refresh Token 삭제 실패 - UserId: {}, Reason: {}", userId, e.getMessage());
        }
    }
}
