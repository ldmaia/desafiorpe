package br.com.rpe.holder.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AggregatedHolderView(
        UUID holderId,
        String holderName,
        String cpf,
        LocalDate birthDate,
        HolderStatus holderStatus,
        UUID productId,
        CardDetails card,
        ProductDetails product,
        Instant createdAt,
        Instant updatedAt
) {
    public record CardDetails(
            UUID cardId,
            String maskedNumber,
            String brand,
            String status
    ) {
    }

    public record ProductDetails(
            UUID id,
            String name,
            String status
    ) {
    }
}
