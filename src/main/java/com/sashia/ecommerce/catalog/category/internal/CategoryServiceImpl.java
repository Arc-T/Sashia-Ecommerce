package com.sashia.ecommerce.catalog.category.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.CategoryRepository;
import com.sashia.ecommerce.catalog.category.CategoryService;
import com.sashia.ecommerce.catalog.category.dto.CategoryCreateRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;
import com.sashia.ecommerce.catalog.category.dto.CategorySearchRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryUpdateRequest;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Long create(CategoryCreateRequest request) {
        Category category = CategoryMapper.toEntity(request);

        if (request.parentId() != null) {

            Category parentCategory = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("category.parentId.not.found"));

            if (parentCategory.getParentId() != null)
                throw new ResourceNotFoundException("category.parentId.invalid");

            category.setParent(parentCategory);
        }

        return categoryRepository.save(category).getId();
    }

    @Override
    public Optional<CategoryResponse> read(Long id) {
        return categoryRepository.findById(id).map(CategoryMapper::toDTO);
    }

    @Override
    public Page<CategoryResponse> readAll(Pageable pageable, CategorySearchRequest search) {
        return categoryRepository.findAll(
                CategorySpecification.hasName(search.name())
                        .and(CategorySpecification.getParents(search.onlyParents()))
                , pageable).map(CategoryMapper::toDTO);
    }

    @Override
    @Transactional
    public void update(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        if (request.parentId() != null) {

            Category parentCategory = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new ResourceNotFoundException("category.parentId.not.found"));

            if (parentCategory.getParentId() != null)
                throw new ResourceNotFoundException("category.parentId.invalid");

            category.setParent(parentCategory);
        }

        CategoryMapper.update(category, request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        if (!category.getItems().isEmpty())
            throw new BusinessRuleException("category.has.assgined.products");

        categoryRepository.delete(category);
    }

}
