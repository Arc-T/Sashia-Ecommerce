package com.sashia.ecommerce.catalog.tag.internal;

import com.sashia.ecommerce.catalog.tag.Tag;
import com.sashia.ecommerce.catalog.tag.dto.TagCreateRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import com.sashia.ecommerce.catalog.tag.dto.TagUpdateRequest;

public class TagMapper {

    public static TagResponse toDTO(Tag tag) {
        return new TagResponse(
                tag.getId(),
                tag.getName()
        );
    }

    public static Tag toEntity(TagCreateRequest dto) {
        Tag tag = new Tag();
        tag.setName(dto.name());
        return tag;
    }

    public static void update(Tag tag, TagUpdateRequest request) {
        tag.setName(request.name());
    }

}
