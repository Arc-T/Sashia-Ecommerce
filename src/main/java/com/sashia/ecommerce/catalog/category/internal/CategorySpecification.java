package com.sashia.ecommerce.catalog.category.internal;

import com.sashia.ecommerce.catalog.category.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class CategorySpecification {

    static Specification<Category> hasName(String name) {
        return (root, _, cb) -> {
            if (!StringUtils.hasText(name))
                return null;

            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    static Specification<Category> getParents(Boolean onlyParents) {
        return (root, _, cb) -> {
            if (Boolean.TRUE.equals(onlyParents))
                return cb.isNull(root.get("parentId"));
            return null;
        };
    }

}
