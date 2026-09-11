package com.sashia.ecommerce.catalog.item.variant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {

    @Query("""
            SELECT iv FROM ItemVariant iv
                   JOIN FETCH iv.item i
            WHERE iv.id = :id
                   AND i.id = :itemId
            """)
    Optional<ItemVariant> findByIdAndItemId(Long id, Long itemId);

}
