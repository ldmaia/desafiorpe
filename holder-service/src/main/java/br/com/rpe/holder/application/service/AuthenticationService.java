package br.com.rpe.holder.application.service;

import br.com.rpe.holder.application.port.in.AuthenticateUserUseCase;
import br.com.rpe.holder.security.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements AuthenticateUserUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public String authenticate(Command command) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(command.username(), command.password()));
        return jwtTokenService.generateToken(command.username());
    }
}
