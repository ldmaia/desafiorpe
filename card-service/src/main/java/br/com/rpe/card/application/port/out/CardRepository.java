package br.com.rpe.card.application.port.out;

import br.com.rpe.card.domain.model.Card;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository {

    Card save(Card card);

    Optional<Card> findByHolderId(UUID holderId);

    boolean existsByHolderId(UUID holderId);
}
