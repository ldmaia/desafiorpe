package br.com.rpe.card.domain.model;

import java.io.Serializable;
import java.util.UUID;

public record ProductSnapshot(
        UUID id,
        String name,
        String status
) implements Serializable {
}
