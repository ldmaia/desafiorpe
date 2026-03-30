package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.domain.model.HolderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.UUID;

public record CreateHolderRequest(
        @NotBlank String name,
        @NotBlank
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 digitos numericos")
        String cpf,
        @NotNull LocalDate birthDate,
        @NotNull HolderStatus status,
        @NotNull UUID productId
) {
}
