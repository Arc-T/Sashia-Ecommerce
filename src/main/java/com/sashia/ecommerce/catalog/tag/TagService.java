package com.sashia.ecommerce.catalog.tag;

import com.sashia.ecommerce.catalog.tag.dto.TagCreateRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import com.sashia.ecommerce.catalog.tag.dto.TagSearchRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TagService {

    Long create(TagCreateRequest tag);

    Optional<TagResponse> read(Long id);

    Page<TagResponse> readAll(TagSearchRequest search, Pageable pageable);

    void update(Long id, TagUpdateRequest tag);

    void delete(Long id);

}