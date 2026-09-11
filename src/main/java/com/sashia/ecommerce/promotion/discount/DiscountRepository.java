package com.sashia.ecommerce.promotion.discount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Optional<Discount> findByPromotionId(Long promotionId);

}
