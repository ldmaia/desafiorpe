package br.com.rpe.product.application.port.in;

import br.com.rpe.product.domain.model.Product;
import br.com.rpe.product.domain.model.ProductStatus;

import java.util.UUID;

public interface UpdateProductStatusUseCase {

    Product updateStatus(UUID id, Command command);

    record Command(ProductStatus status) {
    }
}
