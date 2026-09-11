package com.sashia.ecommerce.catalog.serviceoffering;

import com.sashia.ecommerce.catalog.item.Item;
import jakarta.persistence.*;

@Entity
@Table(name = "service_offerings")
public class ServiceOffering {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private Item item;

    private String name;

    private String description;

    /* ****************************** GETTER & SETTERS ******************************** */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
