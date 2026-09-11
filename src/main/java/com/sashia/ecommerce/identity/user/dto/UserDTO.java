package com.sashia.ecommerce.identity.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String email,
        String phone,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY) Long userGroupId,
        String gender,
        String description,
        LocalDateTime createdAt
) {
}