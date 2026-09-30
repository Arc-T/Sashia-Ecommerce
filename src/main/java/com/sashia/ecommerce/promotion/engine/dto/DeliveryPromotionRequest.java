package com.sashia.ecommerce.promotion.engine.dto;

import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record DeliveryPromotionRequest(@NonNull List<DeliveryOption> deliveryOptions)
        implements PromotionRequest {
}
