package com.sashia.shared.web;

public record ApiResponse(
        String message,
        Object details,
        Integer code
) {
    public ApiResponse(Object details) {
        this(null, details, null);
    }
}
