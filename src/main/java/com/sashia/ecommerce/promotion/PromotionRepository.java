package com.sashia.ecommerce.promotion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("""
            SELECT DISTINCT p FROM Promotion p
                   JOIN FETCH p.type
                   JOIN FETCH p.scope
                   LEFT JOIN FETCH p.targetType ptt
                   LEFT JOIN FETCH p.conditions pc
                        LEFT JOIN FETCH pc.conditionType
                   LEFT JOIN FETCH p.targets pt
            WHERE p.isActive = TRUE
            """)
    List<Promotion> findAllActivePromotions();

}
