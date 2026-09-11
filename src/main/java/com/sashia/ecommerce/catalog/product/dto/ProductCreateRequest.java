package com.sashia.ecommerce.catalog.product.dto;

import java.math.BigDecimal;

public record ProductCreateRequest(String name,
                                   BigDecimal price,
                                   Long quantity,
                                   Long categoryId,
                                   String description) {
}
