package br.com.rpe.card.domain.model;

import java.time.Instant;
import java.util.UUID;

public record Card(
        UUID id,
        UUID holderId,
        UUID productId,
        String maskedNumber,
        String brand,
        CardStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
