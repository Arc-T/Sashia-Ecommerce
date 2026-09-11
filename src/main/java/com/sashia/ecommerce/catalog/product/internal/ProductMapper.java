package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.product.Product;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductPriceDTO;
import com.sashia.ecommerce.catalog.product.dto.ProductSummary;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductCreateRequest request, Category category) {
        Product product = new Product();
//        product.setTitle(request.name());
//        product.setSlug(request.name());
//        product.setPublished(false); //TODO: should be handled later
//        product.setFeatured(false); //TODO: should be handled later
        product.setDescription(request.description());

//        product.setCategory(category);

        return product;
    }

//    public static ProductSummary toSummary(Item item) {
//        return new ProductSummary(
//                item.getId(),
//                item.getTitle(),
//                item.getItemType(),
//                ProductPriceDTO.toDTO(item.getUnitPrice()),
//                item.getCoverImage(),
//                item.isFeatured(),
//                item.getCreatedAt()
//        );
//    }

}
