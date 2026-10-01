package com.sashia.ecommerce.billing.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAuthority(String authority);

    List<Payment> findByOrderId(Long orderId);

    @Query("""
            SELECT p FROM Payment p
                     WHERE p.order.id = :orderId
                           AND p.status = PaymentStatus.PENDING
                     ORDER BY p.createdAt DESC
            """)
    List<Payment> findPendingByOrderId(Long orderId);

    boolean existsByAuthority(String authority);
}
