package com.sashia.ecommerce.catalog.product.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import com.sashia.ecommerce.catalog.item.internal.ProductSearchRequest;
import com.sashia.ecommerce.catalog.product.ProductService;
import com.sashia.ecommerce.catalog.product.dto.ProductBriefInfoProjection;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductDTO;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateDTO;
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

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PRODUCT')")
    ResponseEntity<ProductDTO> create(ProductCreateRequest product) {
        return ResponseEntity.created(URI.create("/products/" + productService.create(product)))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PRODUCT')")
    ResponseEntity<ProductDTO> read(@PathVariable Long id) {
        return ResponseEntity.of(productService.get(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    ResponseEntity<Page<ItemSummaryDTO>> readAll(Pageable pageable, ProductSearchRequest search) {
        return ResponseEntity.ok(productService.getAll(pageable, search));
    }

    @GetMapping("/brief")
    ResponseEntity<Page<ProductBriefInfoProjection>> readAllBrief(Pageable pageable, ProductSearchRequest search) {
        return ResponseEntity.ok(productService.getAllBriefInfo(pageable, search));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PRODUCT')")
    ResponseEntity<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PRODUCT')")
    ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO product) {
        productService.edit(id, product);
        return ResponseEntity.noContent().build();
    }

}