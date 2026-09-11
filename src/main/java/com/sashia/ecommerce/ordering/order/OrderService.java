package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    Long create(CheckoutRequest request);

    Optional<OrderDTO> get(Long id);

    Page<OrderDTO> getAll(Pageable pageable, OrderSearchDTO search);

    void update(Long id, OrderDTO order);

}
