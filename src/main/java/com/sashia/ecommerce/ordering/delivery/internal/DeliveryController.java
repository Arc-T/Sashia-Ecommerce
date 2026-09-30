package com.sashia.ecommerce.ordering.delivery.internal;

import com.sashia.ecommerce.ordering.delivery.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveries")
class DeliveryController {

    private final DeliveryService deliveryService;

    DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping("/methods")
    @PreAuthorize("hasAuthority('READ_ALL_DELIVERY_METHODS')")
    ResponseEntity<Page<DeliveryDTO>> readAll(Pageable pageable) {
        return ResponseEntity.ok(deliveryService.readAll(pageable));
    }

}
