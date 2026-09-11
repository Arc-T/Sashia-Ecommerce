package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

class AttributeSpecification {

    static Specification<Attribute> hasName(String name) {
        return (root, _, cb) -> {
            if (!StringUtils.hasText(name))
                return null;

            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
//
//    static Specification<Attribute> hasCode(AttributeSearchDTO search) {
//        return (root, query, cb) -> {
//            if (search.code() == null || search.code().isBlank()) {
//                return null;
//            }
//
//            return cb.equal(root.get("code"), search.code());
//        };
//    }
//
//    static Specification<Attribute> isActive(AttributeSearchDTO search) {
//        return (root, query, cb) -> {
//            if (search.active() == null) {
//                return null;
//            }
//
//            return cb.equal(root.get("active"), search.active());
//        };
//    }
}