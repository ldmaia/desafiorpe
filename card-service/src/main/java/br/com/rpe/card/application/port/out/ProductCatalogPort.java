package br.com.rpe.card.application.port.out;

import br.com.rpe.card.domain.model.ProductSnapshot;

import java.util.UUID;

public interface ProductCatalogPort {

    ProductSnapshot getById(UUID productId);
}
