package com.sashia.ecommerce.promotion.condition.dto;

import com.sashia.ecommerce.promotion.condition.type.ConditionTypeCode;

public record ConditionDTO(
        ConditionTypeCode type,
        String value) {
}
