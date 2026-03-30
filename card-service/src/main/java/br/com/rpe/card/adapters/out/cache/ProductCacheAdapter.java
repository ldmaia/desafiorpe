package br.com.rpe.card.adapters.out.cache;

import br.com.rpe.card.application.port.out.ProductCachePort;
import br.com.rpe.card.domain.model.ProductSnapshot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Component
public class ProductCacheAdapter implements ProductCachePort {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration ttl;

    public ProductCacheAdapter(
            RedisTemplate<String, Object> redisTemplate,
            @Value("${app.cache.product-ttl-minutes}") long ttlMinutes
    ) {
        this.redisTemplate = redisTemplate;
        this.ttl = Duration.ofMinutes(ttlMinutes);
    }

    @Override
    public Optional<ProductSnapshot> findById(UUID productId) {
        Object cached = redisTemplate.opsForValue().get(cacheKey(productId));
        if (cached instanceof ProductSnapshot productSnapshot) {
            return Optional.of(productSnapshot);
        }
        return Optional.empty();
    }

    @Override
    public void save(ProductSnapshot productSnapshot) {
        redisTemplate.opsForValue().set(cacheKey(productSnapshot.id()), productSnapshot, ttl);
    }

    private String cacheKey(UUID productId) {
        return "product:" + productId;
    }
}
