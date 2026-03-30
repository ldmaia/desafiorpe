package br.com.rpe.card.adapters.out.persistence;

import br.com.rpe.card.domain.model.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardPersistenceMapper {

    CardJpaEntity toEntity(Card card);

    Card toDomain(CardJpaEntity entity);
}
