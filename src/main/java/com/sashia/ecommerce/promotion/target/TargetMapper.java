package com.sashia.ecommerce.promotion.target;

import com.sashia.ecommerce.promotion.target.dto.TargetDTO;

public class TargetMapper {

    public static TargetDTO toDTO(Target target) {
        return new TargetDTO(
                target.getId()
        );
    }

}
