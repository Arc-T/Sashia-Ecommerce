package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaStatus;
import com.sashia.ecommerce.media.dto.MediaSearchRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

final class MediaSpecification {

    private MediaSpecification() {
    }

    static Specification<Media> bySearch(MediaSearchRequest search) {
        return hasStatus(search.status())
                .and(hasResource(search.resourceType(), search.resourceId()))
                .and(hasOwner(search.ownerId()))
                .and(mimeTypeStartsWith(search.mimeType()))
                .and(nameContains(search.name()));
    }

    static Specification<Media> hasStatus(MediaStatus status) {
        return (root, _, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    static Specification<Media> hasOwner(Long ownerId) {
        return (root, _, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    static Specification<Media> hasResource(MediaResourceType type, Long id) {
        return (root, _, cb) -> {
            if (type == null && id == null)
                return null;
            if (type == null)
                return cb.equal(root.get("resourceId"), id);
            if (id == null)
                return cb.equal(root.get("resourceType"), type);
            return cb.and(cb.equal(root.get("resourceType"), type), cb.equal(root.get("resourceId"), id));
        };
    }

    static Specification<Media> mimeTypeStartsWith(String mimeType) {
        return (root, _, cb) -> !StringUtils.hasText(mimeType)
                ? null
                : cb.like(cb.lower(root.get("mimeType")), escape(mimeType.toLowerCase()) + "%", '\\');
    }

    static Specification<Media> nameContains(String name) {
        return (root, _, cb) -> !StringUtils.hasText(name)
                ? null
                : cb.like(cb.lower(root.get("originalName")), "%" + escape(name.toLowerCase()) + "%", '\\');
    }

    /** LIKE wildcards in user input must match literally. */
    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

}
