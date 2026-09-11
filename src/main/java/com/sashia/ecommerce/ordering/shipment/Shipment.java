package com.sashia.ecommerce.ordering.shipment;

import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.promotion.Promotable;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "shipments", schema = "ordering")
public class Shipment implements Promotable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private CurrencyCode currency;

    private BigDecimal cost;

    private String trackingNumber;

    private String carrier;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private ShipmentStatus shipmentStatus;

    /* ********************************** TRANSIENT *********************************** */

    @Transient
    private Integer quantity;

    @Transient
    private List<AppliedPromotion> appliedPromotions;

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @Override
    public List<AppliedPromotion> getAppliedPromotions() {
        return !CollectionUtils.isEmpty(appliedPromotions)
                ? Collections.unmodifiableList(appliedPromotions)
                : Collections.emptyList();
    }

    @Override
    public void addAppliedPromotion(AppliedPromotion appliedPromotion) {
        getAppliedPromotions().add(appliedPromotion);
    }

    @Override
    public BigDecimal getUnitPrice() {
        return cost;
    }

    @Override
    public CurrencyCode getCurrency() {
        return currency;
    }

    @Override
    @Transient
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}