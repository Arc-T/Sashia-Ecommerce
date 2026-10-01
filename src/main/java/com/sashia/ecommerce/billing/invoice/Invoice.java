package com.sashia.ecommerce.billing.invoice;

import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.ordering.order.PricingDetails;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
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
}