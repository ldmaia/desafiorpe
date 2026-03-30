package br.com.rpe.card.adapters.in.sqs;

import java.time.LocalDate;
import java.util.UUID;

public record CardIssuanceMessage(
        UUID holderId,
        UUID productId,
        String holderName,
        String cpf,
        LocalDate birthDate
) {
}
