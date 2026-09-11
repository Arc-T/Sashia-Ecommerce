package com.sashia.ecommerce.billing.invoice;

import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.ordering.order.PricingDetails;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "invoices", schema = "billing")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", insertable = false, updatable = false)
    private Long orderId;

    private String invoiceNumber;

    @Embedded
    private PricingDetails pricingDetails;

    private String billingAddress;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String note;

    @CreationTimestamp
    private LocalDateTime issuedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Order order;

    /* ******************************* TABLE RELATIONS ******************************** */

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    private Set<InvoiceItem> items = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "invoice")
    private Set<InvoiceCharge> charges = new HashSet<>();

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String number) {
        this.invoiceNumber = number;
    }

    public PricingDetails getPricing() {
        return pricingDetails;
    }

    public void setPricing(PricingDetails pricingDetails) {
        this.pricingDetails = pricingDetails;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Set<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(Set<InvoiceItem> items) {
        this.items = items;
    }

    public Set<InvoiceCharge> getCharges() {
        return charges;
    }

    public void setCharges(Set<InvoiceCharge> charges) {
        this.charges = charges;
    }

}