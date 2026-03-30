package br.com.rpe.product.adapters.in.rest;

import br.com.rpe.product.domain.model.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateProductStatusRequest(
        @NotNull
        @Schema(example = "CANCELADO")
        ProductStatus status
) {
}
