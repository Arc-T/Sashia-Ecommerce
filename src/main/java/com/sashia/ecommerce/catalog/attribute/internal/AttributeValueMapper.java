package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.dto.AttributeValueRequest;

public class AttributeValueMapper {

    private AttributeValueMapper() {
    }

    public static AttributeValueRequest toDTO(AttributeValue attributeValue) {
        return new AttributeValueRequest(
                attributeValue.getId(),
                attributeValue.getAttributeValue(),
                attributeValue.getCreatedAt()
        );
    }

    public static AttributeValue toEntity(AttributeValueRequest request) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setAttributeValue(request.value());
        return attributeValue;
    }

}
