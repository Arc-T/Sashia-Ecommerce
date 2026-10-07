package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.CategoryRepository;
import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.item.ItemMapper;
import com.sashia.ecommerce.catalog.item.ItemRepository;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.catalog.item.internal.ItemSpecification;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.catalog.product.Product;
import com.sashia.ecommerce.catalog.product.ProductRepository;
import com.sashia.ecommerce.catalog.product.ProductService;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductResponse;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductVariantRequest;
import com.sashia.ecommerce.catalog.tag.Tag;
import com.sashia.ecommerce.catalog.tag.internal.TagRepository;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaService;
import com.sashia.ecommerce.promotion.engine.PromotionEngine;
import com.sashia.ecommerce.promotion.engine.dto.ItemPromotionRequest;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final MediaService mediaService;
    private final TagRepository tagRepository;
    private final ItemRepository itemRepository;
    private final PromotionEngine promotionEngine;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long create(ProductCreateRequest request) {
        Category category = findProductCategory(request.categoryId());
        Set<Tag> tags = findTags(request.tagIds());

        Product product = productRepository.save(ProductMapper.toEntity(request, category, tags));

        syncMedia(product.getId(), request.mediaIds());
        return product.getId();
    }

    @Override
    public Optional<ProductResponse> read(Long id) {
        return productRepository.findByIdWithItem(id).map(product -> {
            promotionEngine.apply(new ItemPromotionRequest(List.copyOf(product.getItem().getItemVariants())));
            return ProductMapper.toResponse(product, mediaService.readByResource(MediaResourceType.PRODUCT, id));
        });
    }

    @Override
    public Page<ItemSummaryResponse> readAll(Pageable pageable, ProductSearchRequest search) {
        Page<Item> items = itemRepository.findAll(ItemSpecification.bySearch(search), pageable);

        promotionEngine.apply(new ItemPromotionRequest(
                items.stream().flatMap(item -> item.getItemVariants().stream()).toList()));

        return items.map(ItemMapper::toResponse);
    }

    @Override
    @Transactional
    public void update(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findByIdWithItem(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not.found"));

        requireOwnVariants(product, request.variants());
        Category category = findProductCategory(request.categoryId());
        Set<Tag> tags = findTags(request.tagIds());

        ProductMapper.update(product, request, category, tags);

        syncMedia(id, request.mediaIds());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findByIdWithItem(id)
                .orElseThrow(() -> new ResourceNotFoundException("product.not.found"));

        itemRepository.delete(product.getItem()); // soft delete, cascades to the variants
        mediaService.deleteByResource(MediaResourceType.PRODUCT, id);
    }

    // ================================ HELPERS ================================

    /**
     * Drafts become SAVED, saved media that are no longer listed are deleted. null = leave media alone.
     */
    private void syncMedia(Long productId, List<Long> mediaIds) {
        if (mediaIds != null)
            mediaService.sync(MediaResourceType.PRODUCT, productId, mediaIds);
    }

    private Category findProductCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));
        if (category.getItemType() != ItemType.PRODUCT)
            throw new BusinessRuleException("product.category.invalid");
        return category;
    }

    private Set<Tag> findTags(Set<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds))
            return new LinkedHashSet<>();
        List<Tag> tags = tagRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size())
            throw new ResourceNotFoundException("tag.not.found");
        return new LinkedHashSet<>(tags);
    }

    private void requireOwnVariants(Product product, List<ProductVariantRequest> variants) {
        Set<Long> ownIds = product.getItem().getItemVariants().stream()
                .map(ItemVariant::getId).collect(Collectors.toSet());
        for (ProductVariantRequest variant : variants)
            if (variant.id() != null && !ownIds.contains(variant.id()))
                throw new ResourceNotFoundException("product.variant.not.found");
    }

}