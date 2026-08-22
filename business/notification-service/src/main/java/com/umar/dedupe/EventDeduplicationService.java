package com.umar.dedupe;


import com.umar.events.user.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventDeduplicationService implements IEventDeduplicationService{

    private static final long TTL_HOURS = 24;

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean isAlreadyProcessed(UserProfileUpdatedEvent event) {
        String key = buildKey(event);
        Boolean inserted = redisTemplate.opsForValue().setIfAbsent(key, "PROCESSED", TTL_HOURS,TimeUnit.HOURS);
        /*
         * inserted = true
         *      → key did not exist
         *      → this is a NEW event
         *
         * inserted = false
         *      → key already exists
         *      → duplicate event
         */
        boolean duplicate = Boolean.FALSE.equals(inserted);
        log.debug("Dedup check. key={}, duplicate={}", key, duplicate);
        return duplicate;
    }

    @Override
    public void markProcessed(UserProfileUpdatedEvent event) {
        /*
         * Nothing required here because setIfAbsent()
         * already created the key.
         *
         * This method exists only to make the business
         * flow explicit.
         */
    }

    private String buildKey(UserProfileUpdatedEvent event) {
        String operation = "USER_PROFILE_UPDATE";
        String rawKey = event.getEventId()
                        + ":"
                        + event.getUserId()
                        + ":"
                        + operation;
        return "kafka:dedup:" + sha256(rawKey);
    }

    private String sha256(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : hash) {
                result.append(String.format("%02x", b));
            }
            return result.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}
