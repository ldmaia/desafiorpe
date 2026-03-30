package br.com.rpe.holder.domain.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record Holder(
        UUID id,
        String name,
        String cpf,
        LocalDate birthDate,
        HolderStatus status,
        UUID productId,
        Instant createdAt,
        Instant updatedAt
) {
}
