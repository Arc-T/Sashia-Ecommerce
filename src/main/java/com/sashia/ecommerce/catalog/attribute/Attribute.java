package com.sashia.ecommerce.catalog.attribute;

import com.sashia.ecommerce.catalog.attribute.dto.AttributeType;
import com.sashia.ecommerce.catalog.attribute.internal.AttributeValue;
import com.sashia.ecommerce.catalog.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "attributes", schema = "catalog")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    private Boolean isFilterable;

    @Enumerated(EnumType.STRING)
    private AttributeType type;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    /* ******************************* TABLE RELATIONS ******************************** */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AttributeValue> attributeValues = new LinkedHashSet<>();

    /* ********************************** HELPERS ************************************* */

    public void addAttributeValue(AttributeValue value) {
        value.setAttribute(this);
        attributeValues.add(value);
    }

    public void clearAttributeValues() {
        attributeValues.clear();
    }

    /* ****************************** GETTER & SETTERS ******************************** */
}
