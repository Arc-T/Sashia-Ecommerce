package com.sashia.ecommerce.promotion.engine.dto;

public sealed interface PromotionRequest permits CartPromotionRequest,
        ItemPromotionRequest, ShipmentPromotionRequest {

}