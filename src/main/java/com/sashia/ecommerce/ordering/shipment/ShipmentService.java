package com.sashia.ecommerce.ordering.shipment;

import com.sashia.ecommerce.ordering.shipment.internal.ShipmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ShipmentService {

    Optional<ShipmentDTO> read(Long id);

    Page<ShipmentDTO> readAll(Pageable pageable);

}
