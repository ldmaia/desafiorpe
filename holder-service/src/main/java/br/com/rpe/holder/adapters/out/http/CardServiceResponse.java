package br.com.rpe.holder.adapters.out.http;

import java.util.UUID;

public record CardServiceResponse(
        UUID id,
        UUID holderId,
        String maskedNumber,
        String brand,
        String status,
        ProductResponse product
) {
    public record ProductResponse(
            UUID id,
            String name,
            String status
    ) {
    }
}
