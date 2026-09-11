package com.sashia.ecommerce.catalog.category.internal;

import com.sashia.ecommerce.catalog.category.CategoryService;
import com.sashia.ecommerce.catalog.category.dto.CategoryCreateRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;
import com.sashia.ecommerce.catalog.category.dto.CategorySearchRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/categories")
class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    ResponseEntity<Page<CategoryResponse>> readAll(Pageable pageable, CategorySearchRequest search) {
        return ResponseEntity.ok(categoryService.readAll(pageable, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_CATEGORY')")
    ResponseEntity<CategoryResponse> read(@PathVariable Long id) {
        return ResponseEntity.of(categoryService.read(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_CATEGORY')")
    ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest category) {
        return ResponseEntity.created(URI.create("/categories/" + categoryService.create(category)))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_CATEGORY')")
    ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CategoryUpdateRequest category) {
        categoryService.update(id, category);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_CATEGORY')")
    ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}