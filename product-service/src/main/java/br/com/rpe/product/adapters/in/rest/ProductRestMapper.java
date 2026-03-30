package br.com.rpe.product.adapters.in.rest;

import br.com.rpe.product.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductRestMapper {

    ProductResponse toResponse(Product product);
}
