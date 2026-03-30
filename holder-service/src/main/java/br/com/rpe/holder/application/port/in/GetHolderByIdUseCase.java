package br.com.rpe.holder.application.port.in;

import br.com.rpe.holder.domain.model.Holder;

import java.util.UUID;

public interface GetHolderByIdUseCase {

    Holder getById(UUID id);
}
