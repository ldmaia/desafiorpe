package br.com.rpe.holder.domain.exception;

public class CpfAlreadyExistsException extends RuntimeException {

    public CpfAlreadyExistsException(String cpf) {
        super("Ja existe um portador cadastrado com o CPF " + cpf);
    }
}
