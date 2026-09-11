package com.sashia.ecommerce.catalog.attribute.internal;

import com.sashia.ecommerce.catalog.attribute.AttributeService;
import com.sashia.ecommerce.catalog.attribute.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/attributes")
class AttributeController {

    private final AttributeService attributeService;

    AttributeController(AttributeService attributeService) {
        this.attributeService = attributeService;
    }

    // ================================ GET ================================

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_ATTRIBUTES')")
    ResponseEntity<Page<AttributeResponse>> readAll(Pageable pageable, AttributeSearchRequest search) {
        return ResponseEntity.ok(attributeService.readAll(pageable, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ATTRIBUTE')")
    ResponseEntity<AttributeResponse> read(@PathVariable Long id) {
        return ResponseEntity.of(attributeService.read(id));
    }

    // ================================ POST ================================

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ATTRIBUTE')")
    ResponseEntity<AttributeResponse> create(@RequestBody @Valid AttributeCreateRequest attribute) {
        return ResponseEntity.created(URI.create("/attributes/" + attributeService.create(attribute)))
                .build();
    }

    // ================================ DELETE ================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ATTRIBUTE')")
    ResponseEntity<?> delete(@PathVariable Long id) {
        attributeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ================================ PUT ================================

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ATTRIBUTE')")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid AttributeUpdateRequest attribute) {
        attributeService.update(id, attribute);
        return ResponseEntity.noContent().build();
    }

    // ################################## ATTRIBUTE TYPES ##################################

    @GetMapping("/types")
    @PreAuthorize("hasAuthority('READ_ALL_ATTRIBUTE_TYPES')")
    ResponseEntity<AttributeType[]> readAllTypes() {
        return ResponseEntity.ok(AttributeType.values());
    }

}