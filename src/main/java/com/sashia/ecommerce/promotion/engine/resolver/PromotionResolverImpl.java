package com.sashia.ecommerce.promotion.engine.resolver;

import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionResolverImpl implements PromotionResolver {

    private final PromotionRepository promotionRepository;

    public PromotionResolverImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Promotion> resolve() {
        return promotionRepository.findAllActivePromotions();
    }

}
