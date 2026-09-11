package com.sashia.ecommerce.promotion.engine.dto;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.ordering.shipment.Shipment;
import com.sashia.ecommerce.promotion.coupon.Coupon;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;

public record CartPromotionRequest(
        @Nullable Coupon coupon,
        @NonNull Shipment shipment,
        @NonNull List<ItemVariant> itemVariants) implements PromotionRequest {
}
