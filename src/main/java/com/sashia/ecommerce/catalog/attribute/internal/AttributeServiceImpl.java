package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.Attribute;
import com.sashia.ecommerce.catalog.attribute.AttributeService;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeCreateRequest;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeResponse;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeSearchRequest;
import com.sashia.ecommerce.catalog.attribute.dto.AttributeUpdateRequest;
import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.CategoryRepository;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
class AttributeServiceImpl implements AttributeService {

    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;

    public AttributeServiceImpl(CategoryRepository categoryRepository, AttributeRepository attributeRepository) {
        this.categoryRepository = categoryRepository;
        this.attributeRepository = attributeRepository;
    }

    @Override
    @Transactional
    public Long create(AttributeCreateRequest request) {
        Category category = categoryRepository.getReferenceById(request.categoryId());

        Attribute attribute = AttributeMapper.toEntity(request, category);

        if (request.isFilterable()) {

            if (!CollectionUtils.isEmpty(request.values()))
                throw new BusinessRuleException("attribute.values.not.empty");

            for (var value : request.values())
                attribute.addAttributeValue(AttributeValueMapper.toEntity(value));
        }

        return attributeRepository.save(attribute).getId();
    }

    @Override
    public Optional<AttributeResponse> read(Long id) {
        return attributeRepository.findByIdWithFullDetails(id).map(AttributeMapper::toDTO);
    }

    @Override
    public Page<AttributeResponse> readAll(Pageable pageable, AttributeSearchRequest search) {
        return attributeRepository.findAll(AttributeSpecification.hasName(search.name()), pageable).map(AttributeMapper::toDTO);
    }

    @Override
    @Transactional
    public void update(Long id, AttributeUpdateRequest request) {
        Attribute attribute = attributeRepository.findByIdWithFullDetails(id)
                .orElseThrow(() -> new ResourceNotFoundException("attribute.not.found"));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        if (request.isFilterable()) {

            if (!CollectionUtils.isEmpty(request.values()))
                throw new BusinessRuleException("attribute.values.not.empty");

            attribute.clearAttributeValues();

            for (var value : request.values())
                attribute.addAttributeValue(AttributeValueMapper.toEntity(value));
        }

        AttributeMapper.update(attribute, request, category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!attributeRepository.existsById(id))
            throw new ResourceNotFoundException("attribute.not.found"); //TODO: doesn't need to throw exception. just handle manually
        attributeRepository.deleteById(id);
    }

}