package com.sashia.ecommerce.shop.member;

import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.shop.internal.Shop;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shop_members", schema = "shop")
public class ShopMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ************************************** FOREIGN-KEY RELATIONS *******************************************

    @ManyToOne(fetch = FetchType.EAGER)
    private Shop shop;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    /* **************************** GETTER & SETTERS **********************************/

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String description() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Shop shop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public User user() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
