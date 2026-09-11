package com.sashia.ecommerce.shop.dto;

import com.sashia.ecommerce.shop.internal.Shop;

import java.time.LocalDateTime;

public record ShopDTO(
        Long id,
        String name,
        String phone,
        ShopStatus status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {

    public static ShopDTO toDTO(Shop shop) {
        return new ShopDTO(
                shop.id(),
                shop.name(),
                shop.phone(),
                shop.status(),
                shop.description(),
                shop.createdAt(),
                null,
                null
        );
    }
}
