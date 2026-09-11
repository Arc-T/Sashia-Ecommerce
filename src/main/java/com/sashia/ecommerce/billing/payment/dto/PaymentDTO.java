package com.sashia.ecommerce.billing.payment.dto;

import java.time.LocalDateTime;

public record PaymentDTO(
        Long id,
        Long userId,
        Long orderId,
        PaymentMethod paymentMethod,
        Long amount,
        PaymentStatus status,
        String authority,
        String referenceId,
        String feeType,
        String fee,
        String description,
        LocalDateTime createdAt) {
}
