package user_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String BLACKLIST_PREFIX =
            "blacklisted_token:";

    public void blacklistToken(
            String token,
            long expirationMillis) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token,
                "revoked",
                Duration.ofMillis(expirationMillis)
        );
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }

}
