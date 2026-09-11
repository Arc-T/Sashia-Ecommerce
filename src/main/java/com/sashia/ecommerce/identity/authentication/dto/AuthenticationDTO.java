package com.sashia.ecommerce.identity.authentication.dto;

public record AuthenticationDTO(
        String username,
        Boolean hasAccount,
        Boolean hasPassword,
        LoginType loginType,
        String password
) {
}