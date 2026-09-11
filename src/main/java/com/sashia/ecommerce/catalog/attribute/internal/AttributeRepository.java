package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long>, JpaSpecificationExecutor<Attribute> {

    @Query("""
            SELECT a FROM Attribute a
                     JOIN FETCH a.category
                     LEFT JOIN FETCH a.attributeValues
            WHERE a.id = :id
            """)
    Optional<Attribute> findByIdWithFullDetails(Long id);

    @Query("""
            SELECT a FROM Attribute a
                     LEFT JOIN FETCH a.attributeValues
            WHERE a.id = :id
            """)
    Optional<Attribute> findByIdWithValues(Long id);

}