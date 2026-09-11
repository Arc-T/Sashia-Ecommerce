package com.sashia.ecommerce.catalog.serviceoffering.dto;

import java.math.BigDecimal;

public record ServiceOfferingResponse(
        BigDecimal cost,
        Long serviceGroupId
) {
    // ********************** DTOs **********************
//    public static ServiceOfferingResponse toDTO(ServiceOffering serviceOffering) {
//        return new ServiceOfferingResponse(
//                serviceOffering.cost(),
//                serviceOffering.serviceGroupId()
//        );
//    }

}
