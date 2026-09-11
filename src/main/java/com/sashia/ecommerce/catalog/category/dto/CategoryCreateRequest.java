package com.sashia.ecommerce.catalog.category.dto;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryCreateRequest(
        @NotBlank(message = "{category.name.required}")
        String name,
        @NotBlank(message = "{category.url.required}")
        String url,
        @NotNull(message = "{category.catalogItemType.required}")
        ItemType itemType,
        @NotBlank(message = "{category.icon.required}")
        String icon,
        Long parentId,
        @NotNull(message = "{category.displayOrder.required}") @Min(value = 1)
        Integer displayOrder,
        String description
) {
}
