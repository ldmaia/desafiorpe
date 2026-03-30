package br.com.rpe.holder.adapters.in.rest;

import br.com.rpe.holder.domain.exception.AggregationUnavailableException;
import br.com.rpe.holder.domain.exception.CpfAlreadyExistsException;
import br.com.rpe.holder.domain.exception.HolderNotFoundException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HolderNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleHolderNotFound(HolderNotFoundException exception) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), List.of());
    }

    @ExceptionHandler(CpfAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleCpfAlreadyExists(CpfAlreadyExistsException exception) {
        return build(HttpStatus.CONFLICT, exception.getMessage(), List.of());
    }

    @ExceptionHandler(AggregationUnavailableException.class)
    public ResponseEntity<ApiErrorResponse> handleAggregationUnavailable(AggregationUnavailableException exception) {
        return build(HttpStatus.SERVICE_UNAVAILABLE, exception.getMessage(), List.of());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        return build(HttpStatus.UNAUTHORIZED, "Usuario ou senha invalidos.", List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Falha na validacao da requisicao.", details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getTargetType() != null
                && invalidFormatException.getTargetType().isEnum()) {
            String fieldName = invalidFormatException.getPath().isEmpty()
                    ? "campo"
                    : invalidFormatException.getPath().get(invalidFormatException.getPath().size() - 1).getFieldName();
            String acceptedValues = Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            return build(HttpStatus.BAD_REQUEST,
                    "Valor invalido para o campo '" + fieldName + "'. Valores aceitos: " + acceptedValues + ".",
                    List.of());
        }

        return build(HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido.", List.of());
    }

    private String formatFieldError(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, List<String> details) {
        return ResponseEntity.status(status).body(new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                OffsetDateTime.now(),
                details
        ));
    }
}
