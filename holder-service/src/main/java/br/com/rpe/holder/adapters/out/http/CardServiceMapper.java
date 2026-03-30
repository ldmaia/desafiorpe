package br.com.rpe.holder.adapters.out.http;

import br.com.rpe.holder.application.port.out.CardQueryPort;
import br.com.rpe.holder.domain.model.AggregatedHolderView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardServiceMapper {

    @Mapping(target = "cardId", source = "id")
    CardQueryPort.CardProjection toProjection(CardServiceResponse response);

    AggregatedHolderView.ProductDetails toProductDetails(CardServiceResponse.ProductResponse product);
}
