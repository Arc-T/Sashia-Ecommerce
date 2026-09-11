package com.sashia.ecommerce.catalog.attribute.dto;

import org.jspecify.annotations.Nullable;

public record AttributeSearchRequest(
        @Nullable
        String name
) {
}