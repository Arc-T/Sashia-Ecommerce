package com.sashia.ecommerce.promotion.discount;

import com.sashia.ecommerce.promotion.discount.dto.DiscountDTO;

public class DiscountMapper {

    public static DiscountDTO toDTO(Discount discount) {
        return new DiscountDTO(
                discount.getAmount(),
                discount.getType().getCode()
        );
    }

}
