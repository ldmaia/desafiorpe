package br.com.rpe.card.adapters.in.rest;

import br.com.rpe.card.domain.model.CardDetailsView;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardRestMapper {

    CardResponse toResponse(CardDetailsView view);
}
