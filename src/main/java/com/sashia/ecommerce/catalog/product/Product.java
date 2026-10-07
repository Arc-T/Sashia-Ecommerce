package com.sashia.ecommerce.catalog.product;

import com.sashia.ecommerce.catalog.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "products", schema = "catalog")
public class Product {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Item item;

    @Lob
    private String description;

    /* ******************************* TABLE RELATIONS ******************************** */

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
//    private Set<Media> media = new HashSet<>();

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
//    private Set<ProductComments> comments = new HashSet<>();

    /* ****************************** GETTER & SETTERS ******************************** */

}