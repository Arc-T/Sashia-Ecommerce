package com.sashia.ecommerce.promotion.engine.dto;

import com.sashia.ecommerce.ordering.shipment.Shipment;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ShipmentPromotionRequest(@NonNull List<Shipment> shipments) implements PromotionRequest {
}
