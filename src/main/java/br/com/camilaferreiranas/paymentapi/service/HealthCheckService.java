package br.com.camilaferreiranas.paymentapi.service;

import br.com.camilaferreiranas.paymentapi.service.feign.PaymentDefaultClient;
import br.com.camilaferreiranas.paymentapi.service.feign.PaymentFallbackClient;
import br.com.camilaferreiranas.paymentapi.service.feign.dto.ServiceHealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class HealthCheckService {


    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);
    private static final String CACHE_KEY_DEFAULT = "health:default";
    private static final String CACHE_KEY_FALLBACK = "health:fallback";

    private final PaymentDefaultClient defaultClient;
    private final PaymentFallbackClient fallbackClient;
    private final StringRedisTemplate redisTemplate;

    public HealthCheckService(PaymentDefaultClient defaultClient, PaymentFallbackClient fallbackClient, StringRedisTemplate redisTemplate) {
        this.defaultClient = defaultClient;
        this.fallbackClient = fallbackClient;
        this.redisTemplate = redisTemplate;
    }

    public boolean isDefaultHealthy() {
        return checkHealth(CACHE_KEY_DEFAULT, defaultClient::serviceHealth);
    }

    public boolean isFallbackHealthy() {
        return checkHealth(CACHE_KEY_FALLBACK, fallbackClient::serviceHealth);
    }


    private boolean checkHealth(String cacheKey, HealthSupplier healthSupplier) {
        try {
            String cached = redisTemplate.opsForValue().get(cacheKey);
            if(cached != null) {
                return Boolean.parseBoolean(cached);
            }
        } catch (Exception e) {
            log.error("Redis unavailable: {}", e.getMessage());
        }

        boolean healthy = fetchIsHealthy(healthSupplier);

        try {
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(healthy), Duration.ofSeconds(4));
        } catch (Exception e) {
            log.warn("Redis unavailable");
        }

        return healthy;
    }


    private boolean fetchIsHealthy(HealthSupplier supplier) {
        try {
            ServiceHealthResponse health = supplier.get();
            return !health.failing();
        } catch (Exception e) {
            log.warn("Health check failed: {}", e.getMessage());
            return false;
        }
    }


    @FunctionalInterface
    interface HealthSupplier {
        ServiceHealthResponse get();
    }
}
