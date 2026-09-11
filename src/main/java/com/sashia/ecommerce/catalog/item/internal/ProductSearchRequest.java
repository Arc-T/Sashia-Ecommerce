package com.sashia.ecommerce.catalog.item.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import org.jspecify.annotations.NonNull;

public final class ProductSearchRequest extends ItemSearchRequest {

    public ProductSearchRequest(String name, Long categoryId) {
        super(name, categoryId);
    }

    @NonNull
    @Override
    public ItemType getItemType() {
        return ItemType.PRODUCT;
    }

}