package com.sashia.ecommerce.shop.internal;

import com.sashia.ecommerce.catalog.product.Product;
import com.sashia.ecommerce.identity.address.Address;
import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.shop.dto.ShopStatus;
import com.sashia.ecommerce.shop.member.ShopMember;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "shops", schema = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private ShopStatus status;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    /* **************************** FOREIGN-KEY RELATIONS **********************************/

    @OneToOne(fetch = FetchType.LAZY)
    private Address address;

    /* ********************************* TABLE RELATIONS **********************************/

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Order> orders;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Product> products;

    @OneToMany(mappedBy = "shop", fetch = FetchType.LAZY)
    private Set<ShopMember> members;

    /* **************************** GETTER & SETTERS **********************************/

    public Long id() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String phone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ShopStatus status() {
        return status;
    }

    public void setStatus(ShopStatus status) {
        this.status = status;
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

    public LocalDateTime deletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Address address() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Order> orders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<Product> products() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<ShopMember> members() {
        return members;
    }

    public void setMembers(Set<ShopMember> members) {
        this.members = members;
    }

}
