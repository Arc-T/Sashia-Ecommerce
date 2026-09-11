package com.sashia.ecommerce.ordering.shipment.internal;

import com.sashia.ecommerce.ordering.shipment.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipment")
class ShipmentController {

    private final ShipmentService shipmentService;

    ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_SHIPMENT_METHODS')")
    ResponseEntity<Page<ShipmentDTO>> readAll(Pageable pageable) {
        return ResponseEntity.ok(shipmentService.readAll(pageable));
    }

}
