package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import com.sashia.ecommerce.catalog.item.dto.PriceableDTO;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.promotion.PromotionMapper;

public class ItemMapper {

    public static ItemSummaryDTO toDTO(Item item) {
        return new ItemSummaryDTO(
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

    private static ItemVariantDTO toItemVariantDTO(ItemVariant itemVariant) {
        return new ItemVariantDTO(
                itemVariant.getId(),
                itemVariant.getStock(),
                new PriceableDTO(
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
