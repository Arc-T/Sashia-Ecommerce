package com.sashia.ecommerce.ordering.delivery.internal;

import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;

public class DeliveryMapper {

    public static DeliveryDTO toDTO(DeliveryOption itemDeliveryOption) {
        return new DeliveryDTO(
                itemDeliveryOption.getId(),
                itemDeliveryOption.getName(),
                itemDeliveryOption.getCurrency(),
                itemDeliveryOption.getCost(),
                itemDeliveryOption.getDescription(),
                itemDeliveryOption.getAppliedPromotions()
        );
    }

}
