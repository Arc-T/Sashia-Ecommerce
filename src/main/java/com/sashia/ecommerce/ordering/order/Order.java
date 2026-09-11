package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.billing.invoice.Invoice;
import com.sashia.ecommerce.billing.payment.Payment;
import com.sashia.ecommerce.catalog.item.ItemDeliveryOption;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.ecommerce.ordering.order.internal.OrderTransaction;
import com.sashia.ecommerce.promotion.Promotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders", schema = "ordering")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatusType status;

    @Embedded
    private PricingDetails pricing;

    @Embedded
    private DeliveryDetails delivery;

    private String userNote;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ItemDeliveryOption itemDeliveryOption;

    /* ******************************* TABLE RELATIONS ******************************** */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderCharge> orderCharges = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<Payment> payments = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderDetails> orderDetails = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<Invoice> invoices = new LinkedHashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<OrderTransaction> transactions = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_promotions",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private Set<Promotion> promotions = new LinkedHashSet<>();

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatusType getStatus() {
        return status;
    }

    public void setStatus(OrderStatusType status) {
        this.status = status;
    }

    public PricingDetails getPricing() {
        return pricing;
    }

    public void setPricing(PricingDetails pricing) {
        this.pricing = pricing;
    }

    public DeliveryDetails getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryDetails delivery) {
        this.delivery = delivery;
    }

    public String getUserNote() {
        return userNote;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ItemDeliveryOption getItemDeliveryOption() {
        return itemDeliveryOption;
    }

    public void setItemDeliveryOption(ItemDeliveryOption itemDeliveryOption) {
        this.itemDeliveryOption = itemDeliveryOption;
    }

    public Set<OrderCharge> getOrderCharges() {
        return orderCharges;
    }

    public void setOrderCharges(Set<OrderCharge> orderCharges) {
        this.orderCharges = orderCharges;
    }

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> items) {
        this.orderDetails = items;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<OrderTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<OrderTransaction> transactions) {
        this.transactions = transactions;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

}