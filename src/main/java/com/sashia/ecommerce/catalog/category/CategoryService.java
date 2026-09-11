package com.sashia.ecommerce.catalog.category;

import com.sashia.ecommerce.catalog.category.dto.CategoryCreateRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;
import com.sashia.ecommerce.catalog.category.dto.CategorySearchRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {

    Long create(CategoryCreateRequest category);

    Optional<CategoryResponse> read(Long id);

    Page<CategoryResponse> readAll(Pageable pageable, CategorySearchRequest search);

    void update(Long id, CategoryUpdateRequest category);

    void delete(Long id);

}
