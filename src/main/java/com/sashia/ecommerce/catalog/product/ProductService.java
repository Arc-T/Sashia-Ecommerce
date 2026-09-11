package com.sashia.ecommerce.catalog.product;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductBriefInfoProjection;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductDTO;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Long create(ProductCreateRequest product);

    Optional<ProductDTO> get(Long id);

    Page<ItemSummaryDTO> getAll(Pageable pageable, ProductSearchRequest search);

    Page<ProductBriefInfoProjection> getAllBriefInfo(Pageable pageable, ProductSearchRequest search);

    void edit(Long id, ProductUpdateDTO product);

    void delete(Long id);

}
