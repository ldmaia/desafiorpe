package br.com.rpe.product.adapters.out.persistence;

import br.com.rpe.product.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    ProductJpaEntity toEntity(Product product);

    Product toDomain(ProductJpaEntity entity);
}
