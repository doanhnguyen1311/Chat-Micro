package doanh.io.authentication_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class LoginAttemptService {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final Duration TTL = Duration.ofHours(24);
    private final int MAX_FAILS = 5;

    public LoginAttemptService(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String failKey(String userId, String deviceId) {
        return "login:fail:" + userId + ":" + deviceId;
    }

    private String blockKey(String userId, String deviceId) {
        return "login:block:" + userId + ":" + deviceId;
    }

    public boolean isBlocked(String userId, String deviceId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(blockKey(userId, deviceId)));
    }

    public Long incrementFail(String userId, String deviceId) {
        String key = failKey(userId, deviceId);
        Long count = redisTemplate.opsForValue().increment(key);
        if (count == 1) redisTemplate.expire(key, TTL);
        return count;
    }

    public void block(String userId, String deviceId) {
        redisTemplate.opsForValue().set(blockKey(userId, deviceId), TTL);
    }

    public void reset(String userId, String deviceId) {
        redisTemplate.delete(failKey(userId, deviceId));
        redisTemplate.delete(blockKey(userId, deviceId));
    }
}
