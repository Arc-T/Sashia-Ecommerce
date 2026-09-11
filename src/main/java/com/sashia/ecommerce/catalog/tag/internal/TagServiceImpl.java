package com.sashia.ecommerce.catalog.tag.internal;

import com.sashia.ecommerce.catalog.tag.Tag;
import com.sashia.ecommerce.catalog.tag.TagService;
import com.sashia.ecommerce.catalog.tag.dto.TagCreateRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import com.sashia.ecommerce.catalog.tag.dto.TagSearchRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagUpdateRequest;
import com.sashia.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Long create(TagCreateRequest request) {
        return tagRepository.save(TagMapper.toEntity(request)).getId();
    }

    @Override
    public Optional<TagResponse> read(Long id) {
        return tagRepository.findById(id).map(TagMapper::toDTO);
    }

    @Override
    public Page<TagResponse> readAll(TagSearchRequest search, Pageable pageable) {
        return tagRepository.findAll(TagSpecification.hasName(search.name()), pageable)
                .map(TagMapper::toDTO);
    }

    @Override
    @Transactional
    public void update(Long id, TagUpdateRequest request) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tag.not.found"));

        TagMapper.update(tag, request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("tag.not.found"));
        tagRepository.delete(tag);
    }

}