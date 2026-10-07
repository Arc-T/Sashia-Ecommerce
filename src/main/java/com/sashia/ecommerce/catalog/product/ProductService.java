package com.sashia.ecommerce.catalog.product;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductResponse;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Long create(ProductCreateRequest request);

    Optional<ProductResponse> read(Long id);

    Page<ItemSummaryResponse> readAll(Pageable pageable, ProductSearchRequest search);

    void update(Long id, ProductUpdateRequest request);

    void delete(Long id);

}
