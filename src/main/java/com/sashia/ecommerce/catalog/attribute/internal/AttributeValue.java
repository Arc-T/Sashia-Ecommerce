package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attribute_values")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(Long attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String value) {
        this.attributeValue = value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

}
