package br.com.rpe.product.application.port.in;

import br.com.rpe.product.domain.model.Product;

import java.util.UUID;

public interface GetProductByIdUseCase {

    Product getById(UUID id);
}
