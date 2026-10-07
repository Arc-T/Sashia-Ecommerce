package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.product.ProductService;
import com.sashia.ecommerce.catalog.product.dto.ProductBriefInfoProjection;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductResponse;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    ResponseEntity<Page<ItemSummaryResponse>> readAll(Pageable pageable, ProductSearchRequest search) {
        return ResponseEntity.ok(productService.readAll(pageable, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRODUCT')")
    ResponseEntity<ProductResponse> read(@PathVariable Long id) {
        return ResponseEntity.of(productService.read(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    ResponseEntity<Void> create(@RequestBody @Valid ProductCreateRequest request) {
        return ResponseEntity.created(URI.create("/products/" + productService.create(request))).build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ProductUpdateRequest request) {
        productService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}