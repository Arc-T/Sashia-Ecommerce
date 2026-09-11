package com.sashia.ecommerce.ordering.cart;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart", schema = "ordering")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Integer itemCounts;

    private BigDecimal subTotal;

    private BigDecimal totalDiscount;

    private BigDecimal finalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
