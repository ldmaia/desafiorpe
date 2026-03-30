package br.com.rpe.product.adapters.in.rest;

import br.com.rpe.product.domain.model.ProductStatus;

import java.time.Instant;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        ProductStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
