package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.category.Category;
import com.sashia.ecommerce.catalog.category.CategoryRepository;
import com.sashia.ecommerce.catalog.item.Item;
import com.sashia.ecommerce.catalog.item.ItemMapper;
import com.sashia.ecommerce.catalog.item.ItemRepository;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import com.sashia.ecommerce.catalog.item.internal.ItemSpecification;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.product.Product;
import com.sashia.ecommerce.catalog.product.ProductRepository;
import com.sashia.ecommerce.catalog.product.ProductService;
import com.sashia.ecommerce.catalog.product.dto.ProductBriefInfoProjection;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductDTO;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateDTO;
import com.sashia.ecommerce.promotion.engine.PromotionEngine;
import com.sashia.ecommerce.promotion.engine.dto.ItemPromotionRequest;
import com.sashia.shared.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ItemRepository itemRepository;
    private final PromotionEngine promotionEngine;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository,
                              ItemRepository itemRepository, PromotionEngine promotionEngine) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
        this.promotionEngine = promotionEngine;
    }

    @Override
    @Transactional
    public Long create(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("category.not.found"));

        Product product = ProductMapper.toEntity(request, category);

        productRepository.save(product);
//        mediaService.create(productId);
        return product.getId();
    }

    @Override
    public Optional<ProductDTO> get(Long id) {
//        return productRepository.get(id).map(ProductDTO::toDTO)
//                .orElseThrow(() -> new InvalidResourceException("product.not.found"));
        return null;
    }

    @Override
    public Page<ItemSummaryDTO> getAll(Pageable pageable, ProductSearchRequest request) {

        List<Item> items = itemRepository.findAll(ItemSpecification.bySearch(request), pageable)
                .toList();

        promotionEngine.apply(new ItemPromotionRequest(
                items
                        .stream()
                        .map(Item::getItemVariants)
                        .flatMap(Collection::stream)
                        .toList()
        ));

        return new PageImpl<>(items.stream()
                .map(ItemMapper::toDTO)
                .toList(),
                pageable,
                items.size());
    }

    @Override
    public Page<ProductBriefInfoProjection> getAllBriefInfo(Pageable pageable, ProductSearchRequest request) {
        // Page<ProductSummary> productPage = itemRepository.findAll(ItemSpecification.bySearch(request), pageable)
//                .map((item) -> DiscountService.applyDiscountToProducts(item))
        // .map(ProductMapper::toSummary);

//        List<ProductPriceDTO> productPrices = productPriceService.applySellPrice(productPage.getContent());
        return null;
    }

    @Override
    @Transactional
    public void edit(Long id, ProductUpdateDTO product) {
//        productRepository.update(id, product);
//        mediaService.update(id);
    }

    @Override
    public void delete(Long productId) {
    }

}
