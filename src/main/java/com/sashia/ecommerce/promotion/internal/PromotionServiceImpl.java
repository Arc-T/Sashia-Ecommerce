package com.sashia.ecommerce.promotion.internal;

import com.sashia.ecommerce.promotion.PromotionRepository;
import com.sashia.ecommerce.promotion.PromotionService;
import com.sashia.ecommerce.promotion.discount.DiscountRepository;
import com.sashia.ecommerce.promotion.dto.PromotionCreateRequest;
import com.sashia.ecommerce.promotion.dto.PromotionDTO;
import com.sashia.ecommerce.promotion.dto.PromotionUpdateRequest;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PromotionServiceImpl implements PromotionService {

    private final DiscountRepository discountRepository;
    private final PromotionRepository promotionRepository;

    public PromotionServiceImpl(DiscountRepository discountRepository, PromotionRepository promotionRepository) {
        this.discountRepository = discountRepository;
        this.promotionRepository = promotionRepository;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "promotions")
    public Long create(PromotionCreateRequest request) {
        return 0L;
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "promotions")
    public void update(Long id, PromotionUpdateRequest request) {

    }

    @Override
    public Page<PromotionDTO> readAll(Pageable pageable) {
        return null;
    }

}
