package com.sashia.ecommerce.catalog.category.dto;

import org.jspecify.annotations.Nullable;

public record CategorySearchRequest(
        @Nullable String name,
        @Nullable Boolean onlyParents,
        @Nullable Boolean onlyChildren
) {
}