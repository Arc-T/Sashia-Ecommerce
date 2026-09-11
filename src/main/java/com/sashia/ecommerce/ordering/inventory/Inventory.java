package com.sashia.ecommerce.ordering.inventory;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory", schema = "ordering")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ItemVariant itemVariant;

    private String sku;

    private int availableQuantity;

    private int reservedQuantity;

    private int soldQuantity;

    @CreationTimestamp
    private LocalDateTime createdAt;

}