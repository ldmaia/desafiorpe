package br.com.rpe.card.adapters.out.http;

import br.com.rpe.card.domain.model.ProductSnapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductServiceMapper {

    ProductSnapshot toSnapshot(ProductServiceResponse response);
}
