package com.sashia.ecommerce.identity.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest(
        @NotBlank(message = "{username.required}")
        String username,
        @NotBlank(message = "{password.required}") @Length(min = 3, max = 20)
        String password,
        boolean rememberMe
) {
}