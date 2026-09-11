package com.sashia.ecommerce.billing.payment;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethod;
import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.order.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments", schema = "billing")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String authority;

    private String referenceId;

    private String feeType;

    private int fee;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* ******************************** FOREIGN-KEY RELATIONS **************************************/

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    /* **************************** GETTER & SETTERS **********************************/

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethod paymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long amount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public PaymentStatus status() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String authority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String referenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String feeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public int fee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User user() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order order() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
