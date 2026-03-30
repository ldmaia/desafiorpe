package br.com.rpe.product.application.port.out;

import br.com.rpe.product.domain.model.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(UUID id);
}
