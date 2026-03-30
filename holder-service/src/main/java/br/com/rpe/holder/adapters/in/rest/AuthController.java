package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.application.port.in.AuthenticateUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @PostMapping("/token")
    @Operation(summary = "Autentica usuario e devolve JWT")
    public AuthResponse authenticate(@Valid @RequestBody AuthRequest request) {
        String token = authenticateUserUseCase.authenticate(new AuthenticateUserUseCase.Command(
                request.username(),
                request.password()
        ));
        return new AuthResponse(token, "Bearer");
    }
}
