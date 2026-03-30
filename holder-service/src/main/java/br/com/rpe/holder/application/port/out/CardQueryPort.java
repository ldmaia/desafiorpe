package br.com.rpe.holder.application.port.out;

import br.com.rpe.holder.domain.model.AggregatedHolderView;

import java.util.Optional;
import java.util.UUID;

public interface CardQueryPort {

    Optional<CardProjection> findByHolderId(UUID holderId);

    record CardProjection(
            UUID cardId,
            String maskedNumber,
            String brand,
            String status,
            AggregatedHolderView.ProductDetails product
    ) {
    }
}
