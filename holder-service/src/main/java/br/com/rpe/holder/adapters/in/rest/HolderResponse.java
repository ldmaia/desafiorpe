package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.domain.model.HolderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record HolderResponse(
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
