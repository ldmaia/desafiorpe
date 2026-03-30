package br.com.rpe.card.application.port.in;

import java.time.LocalDate;
import java.util.UUID;

public interface ProcessCardIssuanceUseCase {

    void process(Command command);

    record Command(
            UUID holderId,
            UUID productId,
            String holderName,
            String cpf,
            LocalDate birthDate
    ) {
    }
}
