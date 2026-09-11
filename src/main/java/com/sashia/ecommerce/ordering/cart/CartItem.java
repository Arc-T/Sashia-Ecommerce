package com.sashia.ecommerce.ordering.cart;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_item", schema = "ordering")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cartId;

    private Long itemVariantId;

    private BigDecimal price;

    private Integer quantity;

}
