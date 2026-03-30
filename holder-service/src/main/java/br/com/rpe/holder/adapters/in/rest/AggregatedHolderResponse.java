package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.domain.model.HolderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record AggregatedHolderResponse(
        UUID holderId,
        String holderName,
        String cpf,
        LocalDate birthDate,
        HolderStatus holderStatus,
        UUID productId,
        CardResponse card,
        ProductResponse product,
        Instant createdAt,
        Instant updatedAt
) {
    public record CardResponse(UUID id, String maskedNumber, String brand, String status) {
    }

    public record ProductResponse(UUID id, String name, String status) {
    }
}
