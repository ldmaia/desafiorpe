package br.com.rpe.card.adapters.out.http;

import java.time.Instant;
import java.util.UUID;

public record ProductServiceResponse(
        UUID id,
        String name,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
}
