package br.com.rpe.card.domain.exception;

import java.util.UUID;

public class CardAlreadyExistsException extends RuntimeException {

    public CardAlreadyExistsException(UUID holderId) {
        super("Ja existe cartao emitido para o portador " + holderId);
    }
}
