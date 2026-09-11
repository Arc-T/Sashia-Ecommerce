package com.sashia.shared.exception;

import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String FALLBACK_MESSAGE = "unexpected.error";
    private static final String VALIDATION_FALLBACK_MESSAGE = "no.validation.error.message";

    @ExceptionHandler(AuthorizationDeniedException.class)
    ProblemDetail handleAuthorization(AuthorizationDeniedException exception, WebRequest request) {
        return createProblemDetail(exception, HttpStatus.FORBIDDEN, FALLBACK_MESSAGE,
                "authorization.invalid", null, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    ProblemDetail handleAuthentication(AuthenticationException exception, WebRequest request) {
        return createProblemDetail(exception, HttpStatus.UNAUTHORIZED, FALLBACK_MESSAGE,
                "authentication.invalid", null, request);
    }

    @ExceptionHandler(APIException.class)
    ProblemDetail handleAPIException(APIException ex, WebRequest request) {
        return createProblemDetail(ex, toStatus(ex.getErrorCategory()), FALLBACK_MESSAGE,
                ex.getMessageKey(), ex.getMessageArgs(), request);
    }

    @ExceptionHandler(Exception.class)
    ProblemDetail handleUnexpected(Exception exception, WebRequest request) {
        logger.error("Unexpected error", exception);
        return createProblemDetail(exception, HttpStatus.INTERNAL_SERVER_ERROR, "unexpected.error",
                "internal.server.error", null, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        ProblemDetail problem = createProblemDetail(exception, status, FALLBACK_MESSAGE, "invalid.method.params",
                null, request);

        Map<String, List<String>> fieldErrors = extractFieldErrors(exception);
        if (!fieldErrors.isEmpty()) {
            problem.setProperty("fieldErrors", fieldErrors);
        }

        List<String> globalErrors = extractGlobalErrors(exception);
        if (!globalErrors.isEmpty()) {
            problem.setProperty("globalErrors", globalErrors);
        }

        return handleExceptionInternal(
                exception,
                problem,
                headers,
                status,
                request
        );
    }

    private HttpStatusCode toStatus(ErrorCategory type) {
        return switch (type) {
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case BUSINESS_RULE -> HttpStatus.UNPROCESSABLE_CONTENT;
            case CONFLICT -> HttpStatus.CONFLICT;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }

    private List<String> extractGlobalErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getGlobalErrors()
                .stream()
                .map(error -> Optional.ofNullable(error.getDefaultMessage())
                        .orElse(VALIDATION_FALLBACK_MESSAGE))
                .toList();
    }

    private Map<String, List<String>> extractFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        LinkedHashMap::new,
                        Collectors.mapping(
                                error -> Optional.ofNullable(error.getDefaultMessage())
                                        .orElse(VALIDATION_FALLBACK_MESSAGE),
                                Collectors.toList()
                        )
                ));
    }

}