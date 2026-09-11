package com.sashia.ecommerce.catalog.item.variant;

import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.item.internal.ItemVariantAttributeValue;
import com.sashia.ecommerce.catalog.item.variant.status.ItemVariantStatusCode;
import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.ecommerce.ordering.order.OrderDetails;
import com.sashia.ecommerce.promotion.Promotable;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "item_variants", schema = "catalog")
public class ItemVariant implements Promotable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stock;

    @Enumerated(EnumType.STRING)
    private ItemVariantStatusCode status;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    private BigDecimal unitPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @SoftDelete(strategy = SoftDeleteType.TIMESTAMP)
    private LocalDateTime deletedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Item item;

    /* ******************************* TABLE RELATIONS ******************************** */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemVariant")
    private Set<ItemVariantAttributeValue> itemVariantAttributeValues = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemVariant")
    private Set<OrderDetails> orderDetails = new LinkedHashSet<>();

    /* ********************************** TRANSIENT *********************************** */

    @Transient
    private Integer quantity;

    @Transient
    private List<AppliedPromotion> appliedPromotions = new ArrayList<>();

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public ItemVariantStatusCode getStatus() {
        return status;
    }

    public void setStatus(ItemVariantStatusCode status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Set<ItemVariantAttributeValue> getItemVariantAttributeValues() {
        return itemVariantAttributeValues;
    }

    public void setItemVariantAttributeValues(Set<ItemVariantAttributeValue> itemVariantAttributeValues) {
        this.itemVariantAttributeValues = itemVariantAttributeValues;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @Override
    public CurrencyCode getCurrency() {
        return currency;
    }

    @Override
    @Transient
    public List<AppliedPromotion> getAppliedPromotions() {
        return appliedPromotions;
    }

    @Override
    @Transient
    public Integer getQuantity() {
        return quantity == null ? 1 : quantity;
    }

    @Transient
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    @Transient
    public void addAppliedPromotion(AppliedPromotion appliedPromotion) {
        getAppliedPromotions().add(appliedPromotion);
    }

}
