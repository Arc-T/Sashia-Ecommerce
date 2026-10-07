package com.sashia.ecommerce.catalog.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("""
            SELECT p FROM Product p
                   JOIN FETCH p.item i
                   JOIN FETCH i.category
            WHERE p.id = :id
            """)
    Optional<Product> findByIdWithItem(Long id);

}