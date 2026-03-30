package br.com.rpe.holder.application.port.in;

import br.com.rpe.holder.domain.model.AggregatedHolderView;

import java.util.UUID;

public interface GetAggregatedHolderUseCase {

    AggregatedHolderView getAggregated(UUID holderId);
}
