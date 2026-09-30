package com.sashia.shared.exception;

import com.sashia.shared.common.APIError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSource messageSource;

    GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<APIError> handleResourceNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", ex.getMessageKey(), ex.getMessageArgs());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    ResponseEntity<APIError> handleAuthorization(AuthorizationDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "AUTHORIZATION_DENIED", "authorization.invalid", null);
    }

    @ExceptionHandler(AuthenticationException.class)
    ResponseEntity<APIError> handleAuthentication(AuthenticationException ex) {
        return build(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", "authentication.invalid", null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<APIError> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, List<String>> fieldErrors = extractFieldErrors(ex);
        List<String> globalErrors = extractGlobalErrors(ex);

        APIError body = new APIError(
                "VALIDATION_ERROR", resolve("invalid.method.params", null), fieldErrors.isEmpty() ? null : fieldErrors, globalErrors.isEmpty() ? null : globalErrors
        );
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<APIError> handleUnexpected(Exception ex) {
        logger.error("Unexpected error", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "internal.server.error", null);
    }

    private ResponseEntity<APIError> build(HttpStatus status, String error, String messageKey, Object[] args) {
        APIError body = APIError.of(error, resolve(messageKey, args));
        return ResponseEntity.status(status).body(body);
    }

    private String resolve(String key, Object[] args) {
        return messageSource.getMessage(
                key, args, messageSource.getMessage(key, null, key, LocaleContextHolder.getLocale()), LocaleContextHolder.getLocale()
        );
    }

    private List<String> extractGlobalErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getGlobalErrors().stream().map(e -> Optional.ofNullable(e.getDefaultMessage()).orElseGet(() -> resolve("VALIDATION_FALLBACK_MESSAGE", null))).toList();
    }

    private Map<String, List<String>> extractFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream().collect(Collectors.groupingBy(
                FieldError::getField, LinkedHashMap::new, Collectors.mapping(
                        e -> Optional.ofNullable(e.getDefaultMessage()).orElseGet(() -> resolve("VALIDATION_FALLBACK_MESSAGE", null)), Collectors.toList())
        ));
    }
}