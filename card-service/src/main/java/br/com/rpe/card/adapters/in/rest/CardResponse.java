package br.com.rpe.card.adapters.in.rest;

import br.com.rpe.card.domain.model.CardStatus;

import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID holderId,
        String maskedNumber,
        String brand,
        CardStatus status,
        ProductResponse product
) {
    public record ProductResponse(
            UUID id,
            String name,
            String status
    ) {
    }
}
