package com.sashia.ecommerce.promotion.dto;

import java.time.LocalDateTime;

public record PromotionDTO(
        Long id,
        String name,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        String description
        // ********************** RELATIONS **********************
//        TargetTypeCode targetType,
//        DiscountDTO discount,
//        ScopeCode scope,
//        TypeCode type,
//        List<TargetDTO> targets,
//        List<ConditionDTO> conditions
) {
}