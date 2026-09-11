package com.sashia.ecommerce.catalog.attribute;

import com.sashia.ecommerce.catalog.attribute.dto.AttributeCreateRequest;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeResponse;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeSearchRequest;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AttributeService {

    Long create(AttributeCreateRequest attribute);

    Optional<AttributeResponse> read(Long id);

    Page<AttributeResponse> readAll(Pageable pageable, AttributeSearchRequest search);

    void update(Long id, AttributeUpdateRequest attribute);

    void delete(Long id);

}