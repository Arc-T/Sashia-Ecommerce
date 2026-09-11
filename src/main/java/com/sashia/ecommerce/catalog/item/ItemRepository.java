package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    Page<Item> findAllByItemType(Pageable pageable, ItemType itemType);

    @Query("""
            SELECT i FROM Item i
                   JOIN FETCH i.itemVariants iv
            WHERE i.id = :id
                  AND iv.id = :itemVariantId
            """)
    Optional<Item> findByIdAndItemVariantId(Long id, Long itemVariantId);

}
