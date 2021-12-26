package cn.ruiheyun.athena.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ReactiveStringRedisUtils {

    @Autowired
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public Mono<Boolean> set(String key, String value) {
        return reactiveStringRedisTemplate.opsForValue().set(key, value);
    }

    public Mono<Boolean> set(String key, String value, long expire) {
        return reactiveStringRedisTemplate.opsForValue().set(key, value, Duration.of(expire, ChronoUnit.SECONDS));
    }

    public Mono<String> get(String key) {
        return reactiveStringRedisTemplate.opsForValue().get(key).onErrorResume(throwable -> Mono.empty());
    }

    public Mono<Boolean> exist(String key) {
        return reactiveStringRedisTemplate.hasKey(key);
    }

    public Mono<Long> remove(List<String> keys) {
        return remove(keys.toArray(new String[0]));
    }

    public Mono<Long> remove(String...key) {
        return reactiveStringRedisTemplate.delete(key);
    }

    public Mono<Long> increment(String key, long delta) {
        return reactiveStringRedisTemplate.opsForValue().increment(key, delta);
    }

}
