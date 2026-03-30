package br.com.rpe.card.application.port.in;

import br.com.rpe.card.domain.model.CardDetailsView;

import java.util.UUID;

public interface GetCardByHolderIdUseCase {

    CardDetailsView getByHolderId(UUID holderId);
}
