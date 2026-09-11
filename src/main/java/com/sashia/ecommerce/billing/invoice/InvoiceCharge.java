package com.sashia.ecommerce.billing.invoice;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_charges", schema = "billing")
public class InvoiceCharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_id", insertable = false, updatable = false)
    private Long invoiceId;

    @Enumerated(EnumType.STRING)
    private InvoiceChargeType type;

    private BigDecimal amount;

    private String description;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Invoice invoice;

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceChargeType getType() {
        return type;
    }

    public void setType(InvoiceChargeType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

}
