package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.billing.invoice.Invoice;
import com.sashia.ecommerce.billing.payment.Payment;
import com.sashia.ecommerce.ordering.delivery.DeliveryDetails;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.ecommerce.ordering.order.transaction.OrderTransaction;
import com.sashia.ecommerce.promotion.Promotion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
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
    private DeliveryOption deliveryOption;

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
}