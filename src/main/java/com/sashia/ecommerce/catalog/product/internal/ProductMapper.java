package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.item.ItemMapper;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.catalog.item.variant.status.ItemVariantStatusCode;
import com.sashia.ecommerce.catalog.product.Product;
import com.sashia.ecommerce.catalog.product.dto.*;
import com.sashia.ecommerce.catalog.tag.Tag;
import com.sashia.ecommerce.media.dto.MediaResponse;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ProductMapper {

    private ProductMapper() {
    }

    public static Product toEntity(ProductCreateRequest request, Category category, Set<Tag> tags) {
        Item item = new Item();
        item.setItemType(ItemType.PRODUCT);
        item.setTitle(request.title());
        item.setCategory(category);
        item.setFeatured(request.featured());
        item.setTags(tags);
        
        request.variants().forEach(v -> item.addItemVariant(toVariant(v)));

        Product product = new Product();
        product.setItem(item);
        product.setDescription(request.description());
        return product;
    }

    /**
     * Existing variants are matched by id, new ones (id == null) are added, unlisted ones are removed.
     */
    public static void update(Product product, ProductUpdateRequest request, Category category, Set<Tag> tags) {
        Item item = product.getItem();
        item.setTitle(request.title());
        item.setCategory(category);
        item.setFeatured(request.featured());
        item.getTags().clear();
        item.getTags().addAll(tags);
        product.setDescription(request.description());

        Map<Long, ItemVariant> existing = item.getItemVariants().stream()
                .collect(Collectors.toMap(ItemVariant::getId, Function.identity()));
        Set<Long> keptIds = new HashSet<>();

        for (ProductVariantRequest variantRequest : request.variants()) {
            if (variantRequest.id() == null) {
                item.addItemVariant(toVariant(variantRequest));
            } else {
                apply(existing.get(variantRequest.id()), variantRequest);
                keptIds.add(variantRequest.id());
            }
        }
        item.getItemVariants().removeIf(v -> v.getId() != null && !keptIds.contains(v.getId()));
    }

    public static ProductResponse toResponse(Product product, List<MediaResponse> media) {
        Item item = product.getItem();
        return new ProductResponse(
                product.getId(),
                item.getTitle(),
                item.getCategory().getId(),
                item.isFeatured(),
                product.getDescription(),
                item.getTags().stream().map(Tag::getId).collect(Collectors.toCollection(LinkedHashSet::new)),
                item.getItemVariants().stream().map(ItemMapper::toVariantResponse).toList(),
                media,
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }

    // ================================ HELPERS ================================

    private static ItemVariant toVariant(ProductVariantRequest request) {
        ItemVariant variant = new ItemVariant();
        apply(variant, request);
        return variant;
    }

    private static void apply(ItemVariant variant, ProductVariantRequest request) {
        variant.setUnitPrice(request.unitPrice());
        variant.setCurrency(request.currency());
        variant.setStock(request.stock());
        variant.setStatus(request.stock() > 0 ? ItemVariantStatusCode.MARKETABLE : ItemVariantStatusCode.OUT_OF_STOCK);
    }

}
