package com.sashia.ecommerce.billing.payment;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethod;
import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.order.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
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

}
