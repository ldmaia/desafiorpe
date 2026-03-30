package br.com.rpe.holder.domain.exception;

import java.util.UUID;

public class HolderNotFoundException extends RuntimeException {

    public HolderNotFoundException(UUID id) {
        super("Portador nao encontrado para o id " + id);
    }
}
