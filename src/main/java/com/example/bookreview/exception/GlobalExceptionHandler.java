package com.example.bookreview.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<Object> buildError(HttpStatus status, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        Map<String, String> details = new LinkedHashMap<>();
        details.put("code", String.valueOf(status.value()));
        details.put("message", message);
        error.put("error", details);
        return new ResponseEntity<>(error, status);
    }

    // === Custom (domain) exceptions ===
    @ExceptionHandler(AbstractApiException.class)
    public ResponseEntity<Object> handleApiException(AbstractApiException ex) {
        return buildError(ex.getStatus(), ex.getMessage());
    }

    // === Validation and input errors ===
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("Validation error occurred", ex);

        // Uzimamo sve stvarne validacione poruke
        List<String> messages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());

        String message = String.join(", ", messages);

        return ResponseEntity.badRequest().body(
                Map.of("error", Map.of("code", "400", "message", message))
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint violation", ex);
        String message = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + " " + v.getMessage())
                .collect(Collectors.joining(", "));
        return buildError(HttpStatus.BAD_REQUEST, "Invalid request: " + message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleEmptyBody(HttpMessageNotReadableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, "Request body is missing or malformed JSON");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        log.error("Missing req parameter",ex);
        String message = "Missing required parameter '" + ex.getParameterName() + "'";
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("Invalid parameter!",ex);
        String param = ex.getName();
        String requiredType = ex.getRequiredType() != null
                ? ex.getRequiredType().getSimpleName()
                : "expected type";
        String message = "Invalid parameter '" + param + "': must be a valid " + requiredType;
        return buildError(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        log.error("Bad request!", ex);
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // === Generic fallback ===
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        System.err.println("IMAMO GRESKU!\n"+ex.getMessage());
        ex.printStackTrace();
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "Resource not found: " + ex.getResourcePath());
    }
}