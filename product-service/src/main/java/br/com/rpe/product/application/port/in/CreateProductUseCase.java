package br.com.rpe.product.application.port.in;

import br.com.rpe.product.domain.model.Product;
import br.com.rpe.product.domain.model.ProductStatus;

public interface CreateProductUseCase {

    Product create(Command command);

    record Command(String name, ProductStatus status) {
    }
}
