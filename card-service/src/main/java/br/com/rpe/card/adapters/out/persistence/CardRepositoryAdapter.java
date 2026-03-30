package br.com.rpe.card.adapters.out.persistence;

import br.com.rpe.card.application.port.out.CardRepository;
import br.com.rpe.card.domain.model.Card;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CardRepositoryAdapter implements CardRepository {

    private final CardJpaRepository cardJpaRepository;
    private final CardPersistenceMapper cardPersistenceMapper;

    public CardRepositoryAdapter(CardJpaRepository cardJpaRepository, CardPersistenceMapper cardPersistenceMapper) {
        this.cardJpaRepository = cardJpaRepository;
        this.cardPersistenceMapper = cardPersistenceMapper;
    }

    @Override
    public Card save(Card card) {
        return cardPersistenceMapper.toDomain(cardJpaRepository.save(cardPersistenceMapper.toEntity(card)));
    }

    @Override
    public Optional<Card> findByHolderId(UUID holderId) {
        return cardJpaRepository.findByHolderId(holderId).map(cardPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByHolderId(UUID holderId) {
        return cardJpaRepository.existsByHolderId(holderId);
    }
}
