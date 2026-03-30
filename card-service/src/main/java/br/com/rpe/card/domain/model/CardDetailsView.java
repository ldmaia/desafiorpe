package br.com.rpe.card.domain.model;

import java.util.UUID;

public record CardDetailsView(
        UUID id,
        UUID holderId,
        String maskedNumber,
        String brand,
        CardStatus status,
        ProductSnapshot product
) {
}
