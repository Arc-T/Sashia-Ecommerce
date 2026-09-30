package com.sashia.ecommerce.ordering.order.status;

import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    @Query("""
            SELECT os FROM OrderStatus os
                      WHERE os.type = :type
                            AND os.active = true
            """)
    Optional<OrderStatus> findByType(OrderStatusType type);

}
