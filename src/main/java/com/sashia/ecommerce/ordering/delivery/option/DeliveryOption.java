package com.sashia.ecommerce.ordering.delivery.option;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.ordering.delivery.DeliveryStatus;
import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.promotion.Promotable;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "delivery_options", schema = "ordering")
public class DeliveryOption implements Promotable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private CurrencyCode currency;

    private BigDecimal cost;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* ******************************* TABLE RELATIONS ******************************** */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryOption")
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryOption")
    private Set<DeliveryStatus> deliveryStatuses = new LinkedHashSet<>();

    /* ********************************** TRANSIENT *********************************** */

    @Transient
    private List<AppliedPromotion> appliedPromotions = new ArrayList<>();

    @Override
    public List<AppliedPromotion> getAppliedPromotions() {
        return appliedPromotions;
    }

    @Override
    public void addAppliedPromotion(AppliedPromotion appliedPromotion) {
        throw new UnsupportedOperationException("Unimplemented method 'addAppliedPromotion'");
    }

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType type) {
        this.itemType = type;
    }

    public void setCurrency(CurrencyCode currency) {
        this.currency = currency;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal unitPrice) {
        this.cost = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<DeliveryStatus> getDeliveryStatuses() {
        return deliveryStatuses;
    }

    public void setDeliveryStatuses(Set<DeliveryStatus> deliveryStatuses) {
        this.deliveryStatuses = deliveryStatuses;
    }

    public void setAppliedPromotions(List<AppliedPromotion> appliedPromotions) {
        this.appliedPromotions = appliedPromotions;
    }

    @Override
    public Integer getQuantity() {
        return 1;
    }

    @Override
    public BigDecimal getUnitPrice() {
        return cost;
    }

    @Override
    public CurrencyCode getCurrency() {
        return currency;
    }

}
