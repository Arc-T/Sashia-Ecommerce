package com.sashia.ecommerce.catalog.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ProductUpdateRequest(
        @NotBlank(message = "{product.title.required}")
        String title,
        @NotNull(message = "{category.id.required}")
        Long categoryId,
        String description,
        boolean featured,
        @Nullable Set<Long> tagIds,
        @NotEmpty(message = "{product.variants.required}")
        @Valid List<ProductVariantRequest> variants,
        /* null = leave media untouched, [] = remove all, otherwise the exact ordered list */
        @Nullable @Size(max = 50) List<Long> mediaIds
) {
}
