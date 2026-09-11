package com.sashia.ecommerce.catalog.serviceoffering.internal;

import com.sashia.ecommerce.catalog.item.internal.ServiceOfferingSearchRequest;
import com.sashia.ecommerce.catalog.serviceoffering.ServiceOfferingService;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingCreateRequest;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingResponse;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceUpdateDTO;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/service-offerings")
class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_SERVICES')")
    ResponseEntity<Page<ServiceOfferingResponse>> readAll(Pageable pageable, ServiceOfferingSearchRequest search) {
        return ResponseEntity.ok(serviceOfferingService.getAll(pageable, search));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_SERVICE')")
    ResponseEntity<ServiceOfferingResponse> read(@PathVariable Long id) {
        return ResponseEntity.ok(serviceOfferingService.get(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_SERVICE')")
    ResponseEntity<TagResponse> create(@RequestBody @Valid ServiceOfferingCreateRequest service) {
        return ResponseEntity.created(URI.create("/services/" + serviceOfferingService.create(service)))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_SERVICE')")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid ServiceUpdateDTO service) {
        serviceOfferingService.edit(id, service);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_SERVICE')")
    ResponseEntity<?> delete(@PathVariable Long id) {
        serviceOfferingService.delete(id);
        return ResponseEntity.noContent().build();
    }

}