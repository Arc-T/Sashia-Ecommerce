package com.sashia.ecommerce.catalog.item.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import org.jspecify.annotations.NonNull;

public final class ServiceOfferingSearchRequest extends ItemSearchRequest {

    ServiceOfferingSearchRequest(String name, Long categoryId) {
        super(name, categoryId);
    }

    @NonNull
    @Override
    public ItemType getItemType() {
        return ItemType.SERVICE_OFFERING;
    }

}
