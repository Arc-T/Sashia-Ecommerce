package com.sashia.ecommerce.promotion.engine.dto;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ItemPromotionRequest(@NonNull List<ItemVariant> itemVariants) implements PromotionRequest {
}
