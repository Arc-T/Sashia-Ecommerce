package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderSearchDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    Long create(CheckoutRequest request);

    Optional<OrderDTO> get(Long id);

    Page<OrderDTO> getAll(Pageable pageable, OrderSearchDTO search);

    void update(Long id, OrderDTO order);

    /**
     * Move an order to a new status and append an {@code OrderTransaction} audit row.
     *
     * @param orderId     target order
     * @param newStatus   status to apply
     * @param description human-readable reason (stored on the transaction)
     */
    void transitionStatus(Long orderId, OrderStatusType newStatus, String description);

}
