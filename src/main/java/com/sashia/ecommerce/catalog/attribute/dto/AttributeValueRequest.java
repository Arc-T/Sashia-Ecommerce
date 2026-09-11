package com.sashia.ecommerce.catalog.attribute.dto;

import java.time.LocalDateTime;

public record AttributeValueRequest(
        Long id,
        String value,
        LocalDateTime createdAt
) {

    public AttributeValueRequest(String value) {
        this(null, value, null);
    }

}
