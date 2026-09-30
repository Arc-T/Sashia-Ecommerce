package com.sashia.ecommerce.ordering.delivery;

import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "delivery_transactions", schema = "ordering")
public class DeliveryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private DeliveryOption DeliveryOption;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private DeliveryStatus deliveryStatus;

    /* ****************************** GETTER & SETTERS ******************************** */


}