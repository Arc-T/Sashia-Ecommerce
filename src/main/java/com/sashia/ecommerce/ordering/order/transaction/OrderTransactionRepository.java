package com.sashia.ecommerce.ordering.order.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTransactionRepository extends JpaRepository<OrderTransaction, Long> {
}
