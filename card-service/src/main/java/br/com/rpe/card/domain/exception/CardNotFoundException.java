package br.com.rpe.card.domain.exception;

import java.util.UUID;

public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(UUID holderId) {
        super("Cartao nao encontrado para o portador " + holderId);
    }
}
