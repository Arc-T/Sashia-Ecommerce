package com.sashia.ecommerce.catalog.category.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.dto.CategoryCreateRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;
import com.sashia.ecommerce.catalog.category.dto.CategoryUpdateRequest;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toEntity(CategoryUpdateRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.url());
        category.setDisplayOrder(request.displayOrder());
        category.setIcon(request.icon());
        category.setDescription(request.description());
        return category;
    }


    public static Category toEntity(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setSlug(request.url());
        category.setDisplayOrder(request.displayOrder());
        category.setIcon(request.icon());
        category.setDescription(request.description());
        return category;
    }

    public static CategoryResponse toDTO(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getIcon(),
                category.getParentId(),
                category.getDisplayOrder(),
                category.getDescription(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }

    public static void update(Category category, CategoryUpdateRequest request) {
        category.setName(request.name());
        category.setSlug(request.url());
        category.setIcon(request.icon());
        category.setDisplayOrder(request.displayOrder());
        category.setDescription(request.description());
    }

}