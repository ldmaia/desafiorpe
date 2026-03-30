package br.com.rpe.card.adapters.in.rest;

import java.time.OffsetDateTime;
import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        OffsetDateTime timestamp,
        List<String> details
) {
}
