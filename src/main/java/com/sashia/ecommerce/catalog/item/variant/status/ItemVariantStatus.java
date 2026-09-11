// package com.sashia.ecommerce.catalog.item.variant.status;

// import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
// import jakarta.persistence.*;
// import org.hibernate.annotations.CreationTimestamp;

// import java.time.LocalDateTime;
// import java.util.LinkedHashSet;
// import java.util.Set;

// @Entity
// @Table(name = "item_variant_statuses", schema = "catalog")
// public class ItemVariantStatus {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Enumerated(EnumType.STRING)
//     private ItemVariantStatusCode code;

//     private String title;

//     private String description;

//     @CreationTimestamp
//     private LocalDateTime createdAt;

//     /* ******************************* TABLE RELATIONS ******************************** */

//     @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemVariantStatus")
//     private Set<ItemVariant> itemVariants = new LinkedHashSet<>();

//     /* ****************************** GETTER & SETTERS ******************************** */

//     public Long getId() {
//         return id;
//     }

//     public void setId(Long id) {
//         this.id = id;
//     }

//     public ItemVariantStatusCode getCode() {
//         return code;
//     }

//     public void setCode(ItemVariantStatusCode code) {
//         this.code = code;
//     }

//     public String getTitle() {
//         return title;
//     }

//     public void setTitle(String title) {
//         this.title = title;
//     }

//     public String getDescription() {
//         return description;
//     }

//     public void setDescription(String description) {
//         this.description = description;
//     }

//     public LocalDateTime getCreatedAt() {
//         return createdAt;
//     }

//     public void setCreatedAt(LocalDateTime createdAt) {
//         this.createdAt = createdAt;
//     }

//     public Set<ItemVariant> getItemVariants() {
//         return itemVariants;
//     }

//     public void setItemVariants(Set<ItemVariant> itemVariants) {
//         this.itemVariants = itemVariants;
//     }

// }
