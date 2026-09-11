package com.sashia.ecommerce.catalog.serviceoffering.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ServiceOfferingCreateRequest(
        @NotBlank(message = "{service.name.required}")
        String name,
        @NotNull(message = "{service.cost.required}") @Min(value = 0)
        BigDecimal cost,
        @NotNull(message = "{service.serviceGroup.required}")
        Long serviceGroupId,
        String description
) {
}