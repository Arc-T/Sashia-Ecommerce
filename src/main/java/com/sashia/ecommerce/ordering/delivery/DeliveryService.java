package com.sashia.ecommerce.ordering.delivery;

import com.sashia.ecommerce.ordering.delivery.internal.DeliveryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeliveryService {

    Optional<DeliveryDTO> read(Long id);

    Page<DeliveryDTO> readAll(Pageable pageable);

}
