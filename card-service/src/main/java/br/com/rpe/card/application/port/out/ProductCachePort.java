package br.com.rpe.card.application.port.out;

import br.com.rpe.card.domain.model.ProductSnapshot;

import java.util.Optional;
import java.util.UUID;

public interface ProductCachePort {

    Optional<ProductSnapshot> findById(UUID productId);

    void save(ProductSnapshot productSnapshot);
}
