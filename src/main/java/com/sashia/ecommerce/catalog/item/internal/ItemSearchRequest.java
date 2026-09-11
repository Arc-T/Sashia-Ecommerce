package com.sashia.ecommerce.catalog.item.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import org.jspecify.annotations.NonNull;

public abstract sealed class ItemSearchRequest permits ProductSearchRequest,
        ServiceOfferingSearchRequest {

    private final String name;
    private final Long categoryId;

    protected ItemSearchRequest(String name, Long categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @NonNull
    public abstract ItemType getItemType();

}
