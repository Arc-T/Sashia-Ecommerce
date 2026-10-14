package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "attribute_values", schema = "catalog")
public class AttributeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute_id", insertable = false, updatable = false)
    private Long attributeId;

    private String attributeValue;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* ******************************* TABLE RELATIONS ******************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Attribute attribute;

    /* ****************************** GETTER & SETTERS ******************************** */

}
