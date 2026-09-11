package com.sashia.ecommerce.catalog.category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequest(
        @NotBlank(message = "{category.name.required}")
        String name,
        @NotBlank(message = "{category.url.required}")
        String url,
        @NotBlank(message = "{category.icon.required}")
        String icon,
        Long parentId,
        @NotNull(message = "{category.displayOrder.required}") @Min(value = 1)
        Integer displayOrder,
        String description
) {
}
