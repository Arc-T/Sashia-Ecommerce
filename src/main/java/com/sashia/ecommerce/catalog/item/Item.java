package com.sashia.ecommerce.catalog.item;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.catalog.tag.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "items", schema = "catalog")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String title;

    private boolean isFeatured;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @SoftDelete(strategy = SoftDeleteType.TIMESTAMP)
    private LocalDateTime deletedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;

    /* ******************************* TABLE RELATIONS ******************************** */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<ItemVariant> itemVariants = new LinkedHashSet<>();

    /* *********************************** HELPERS ************************************ */

    @Transient
    public ItemVariant getDefaultItemVariant() {
        return itemVariants.stream().findFirst().orElse(null);
    }

    @Transient
    public void addItemVariant(ItemVariant variant) {
        variant.setItem(this);
        itemVariants.add(variant);
    }

    /* ****************************** GETTER & SETTERS ******************************** */

}