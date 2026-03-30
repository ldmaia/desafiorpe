package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.domain.model.AggregatedHolderView;
import br.com.rpe.holder.domain.model.Holder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HolderRestMapper {

    HolderResponse toResponse(Holder holder);

    @Mapping(target = "card.id", source = "card.cardId")
    AggregatedHolderResponse toAggregatedResponse(AggregatedHolderView view);
}
