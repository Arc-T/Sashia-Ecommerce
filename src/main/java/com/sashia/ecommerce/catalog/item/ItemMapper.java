package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.catalog.item.dto.PriceResponse;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.promotion.PromotionMapper;

public class ItemMapper {

    public static ItemSummaryResponse toResponse(Item item) {
        return new ItemSummaryResponse(
                item.getId(),
                item.getTitle(),
                item.getCategory().getId(),
                item
                        .getItemVariants()
                        .stream()
                        .map(ItemMapper::toItemVariantDTO)
                        .toList()
        );
    }

    private static ItemVariantResponse toItemVariantDTO(ItemVariant itemVariant) {
        return new ItemVariantResponse(
                itemVariant.getId(),
                itemVariant.getStock(),
                new PriceResponse(
                        itemVariant.getUnitPrice(),
                        itemVariant.calculateSubTotal(),
                        itemVariant.calculateTotalDiscountAmount(),
                        itemVariant.calculateTotal(),
                        itemVariant.getCurrency(),
                        !itemVariant
                                .getAppliedPromotions().isEmpty() ?
                                itemVariant.getAppliedPromotions()
                                        .stream()
                                        .map(PromotionMapper::toDTO)
                                        .toList()
                                : null
                )
        );
    }
}
