package br.com.rpe.holder.application.port.in;

import br.com.rpe.holder.domain.model.Holder;
import br.com.rpe.holder.domain.model.HolderStatus;

import java.time.LocalDate;
import java.util.UUID;

public interface CreateHolderUseCase {

    Holder create(Command command);

    record Command(
            String name,
            String cpf,
            LocalDate birthDate,
            HolderStatus status,
            UUID productId
    ) {
    }
}
