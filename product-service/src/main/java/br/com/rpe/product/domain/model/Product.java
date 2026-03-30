package br.com.rpe.product.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Product(
        UUID id,
        String name,
        ProductStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
