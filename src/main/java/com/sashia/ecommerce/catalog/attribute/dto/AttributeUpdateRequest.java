package com.sashia.ecommerce.catalog.attribute.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AttributeUpdateRequest(
        @NotEmpty(message = "{attribute.name.required}")
        String name,
        @NotNull(message = "{category.id.required}")
        Long categoryId,
        @NotNull(message = "{attribute.type.required}")
        AttributeType type,
        @NotNull(message = "{attribute.isFilterable.required}")
        Boolean isFilterable,
        String description,
        List<AttributeValueRequest> values
) {
}
