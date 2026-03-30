package br.com.rpe.holder.application.port.out;

import java.time.LocalDate;
import java.util.UUID;

public interface CardIssuancePublisher {

    void publish(CardIssuanceRequested event);

    record CardIssuanceRequested(
            UUID holderId,
            UUID productId,
            String holderName,
            String cpf,
            LocalDate birthDate
    ) {
    }
}
