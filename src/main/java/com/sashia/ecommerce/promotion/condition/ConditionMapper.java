package com.sashia.ecommerce.promotion.condition;

import com.sashia.ecommerce.promotion.condition.dto.ConditionDTO;

public class ConditionMapper {

    public static ConditionDTO toDTO(Condition condition) {
        return new ConditionDTO(
                condition.getConditionType().getCode(),
                condition.getConditionValue()
        );
    }

}
