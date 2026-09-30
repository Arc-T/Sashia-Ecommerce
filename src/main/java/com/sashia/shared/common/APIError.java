package com.sashia.shared.common;

import java.util.List;
import java.util.Map;

public record APIError(
                       String error,
                       String message,
                       Map<String, List<String>> fieldErrors,
                       List<String> globalErrors) {
    public static APIError of(String error, String message) {
        return new APIError(error, message, null, null);
    }
}