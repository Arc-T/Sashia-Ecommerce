package com.sashia.ecommerce.catalog.product.dto;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public record ProductBriefInfoProjection(
        long id,
        String name,
        String media,
        int quantity,
        ProductPriceDTO price
) {

    public static List<ProductBriefInfoProjection> toListDTO(List<ProductDTO> products, List<ProductPriceDTO> productPrices) {
        if (!CollectionUtils.isEmpty(products) &&
                !CollectionUtils.isEmpty(productPrices) &&
                products.size() == productPrices.size()) {
            List<ProductBriefInfoProjection> productBriefInfoProjections = new ArrayList<>(products.size());
            for (int i = 0; i < products.size(); i++) {
                productBriefInfoProjections.add(new ProductBriefInfoProjection(
                        products.get(i).id(),
                        products.get(i).name(),
                        products.get(i).media().stream().findFirst().get().url(),
                        products.get(i).stock(),
                        productPrices.get(i)
                ));
            }
            return productBriefInfoProjections;
        } else
            throw new IllegalStateException("Products and prices are not equal");
    }

}