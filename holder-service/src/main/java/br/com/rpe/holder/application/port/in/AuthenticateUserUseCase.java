package br.com.rpe.holder.application.port.in;

public interface AuthenticateUserUseCase {

    String authenticate(Command command);

    record Command(String username, String password) {
    }
}
