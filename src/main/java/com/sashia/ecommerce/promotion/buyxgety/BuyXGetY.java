package com.sashia.ecommerce.promotion.buyxgety;

import com.sashia.ecommerce.promotion.Promotion;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "buy_x_get_y", schema = "promotion")
public class BuyXGetY {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Quantity the customer must purchase.
     */
    private Integer buyQuantity;

    /**
     * Quantity rewarded.
     */
    private Integer rewardQuantity;

    /**
     * Whether the reward is completely free.
     * If false, another reward configuration determines
     * the discount applied to the reward items.
     */
    private boolean free;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* **************************** FOREIGN-KEY RELATIONS ***************************** */

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Promotion promotion;

    /* ******************************* TABLE RELATIONS ******************************** */

    /* ****************************** GETTER & SETTERS ******************************** */

}
