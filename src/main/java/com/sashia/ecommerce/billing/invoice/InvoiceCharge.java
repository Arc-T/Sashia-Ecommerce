package com.sashia.ecommerce.billing.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
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
}
