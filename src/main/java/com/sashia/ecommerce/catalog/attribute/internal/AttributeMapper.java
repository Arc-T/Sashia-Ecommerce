package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeCreateRequest;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeResponse;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeUpdateRequest;
import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.internal.CategoryMapper;

import java.util.stream.Collectors;

public final class AttributeMapper {

    private AttributeMapper() {
    }

    public static AttributeResponse toDTO(Attribute attribute) {
        return new AttributeResponse(
                attribute.getId(),
                attribute.getName(),
                attribute.getCategoryId(),
                attribute.getType(),
                attribute.getIsFilterable(),
                attribute.getDescription(),
                attribute.getCreatedAt(),
                attribute.getUpdatedAt(),
                CategoryMapper.toDTO(attribute.getCategory()),
                attribute.getAttributeValues()
                        .stream().map(AttributeValueMapper::toDTO).collect(Collectors.toUnmodifiableSet())
        );
    }

    public static Attribute toEntity(AttributeCreateRequest dto, Category category) {
        Attribute attribute = new Attribute();
        attribute.setName(dto.name());
        attribute.setCategory(category);
        attribute.setIsFilterable(dto.isFilterable());
        attribute.setType(dto.type());
        attribute.setDescription(dto.description());
        return attribute;
    }

    public static void update(Attribute attribute, AttributeUpdateRequest request, Category category) {
        attribute.setName(request.name());
        attribute.setType(request.type());
        attribute.setDescription(request.description());
        attribute.setIsFilterable(request.isFilterable());
        attribute.setCategory(category);
    }

}