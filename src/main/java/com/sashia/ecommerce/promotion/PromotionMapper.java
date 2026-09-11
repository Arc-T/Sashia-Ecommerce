package com.sashia.ecommerce.promotion;

import com.sashia.ecommerce.catalog.item.dto.AppliedPromotionDTO;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

public class PromotionMapper {

    public static AppliedPromotionDTO toDTO(AppliedPromotion appliedPromotion) {
        return new AppliedPromotionDTO(
                appliedPromotion.promotion().getId(),
                appliedPromotion.promotion().getName(),
                appliedPromotion.promotion().getValidFrom(),
                appliedPromotion.promotion().getValidUntil(),
                appliedPromotion.promotion().getDescription(),
                appliedPromotion.discountAmount(),
                appliedPromotion.priceBefore(),
                appliedPromotion.priceAfter()
        );
    }
//
//    public static PromotionDTO toDTO(Promotion promotion, Discount discount) {
//
//        List<TargetDTO> targets = promotion.getTargets().isEmpty() ? List.of() :
//                promotion.getTargets().stream().map(TargetMapper::toDTO).toList();
//
//        List<ConditionDTO> conditions = promotion.getConditions().isEmpty() ? List.of() :
//                promotion.getConditions().stream().map(ConditionMapper::toDTO).toList();
//
//        return new PromotionDTO(
//                promotion.getId(),
//                promotion.getName(),
//                promotion.getValidFrom(),
//                promotion.getValidUntil(),
//                promotion.isActive(),
//                promotion.getDescription(),
//                promotion.getTargetType().getCode(),
//                DiscountMapper.toDTO(discount),
//                promotion.getScope().getCode(),
//                promotion.getType().getCode(),
//                targets,
//                conditions
//        );
//    }

}