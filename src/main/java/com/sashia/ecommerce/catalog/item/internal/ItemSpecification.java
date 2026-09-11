package com.sashia.ecommerce.catalog.item.internal;

import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ItemSpecification {

    public static Specification<Item> bySearch(ItemSearchRequest request) {
        return Specification
                .where(hasType(request.getItemType()));
    }

    private static Specification<Item> hasType(ItemType type) {
        return (root, _, cb) ->
                cb.equal(root.get("itemType"), type);
    }

    private static Specification<Item> hasCategory(Long categoryId) {
        return (root, _, cb) ->
                categoryId == null
                        ? null
                        : cb.equal(root.get("categoryId"), categoryId);
    }

    private static Specification<Item> titleContains(String search) {
        return (root, _, cb) ->
                StringUtils.hasText(search)
                        ? cb.like(cb.lower(root.get("title")),
                        "%" + search.toLowerCase() + "%")
                        : null;
    }

}
