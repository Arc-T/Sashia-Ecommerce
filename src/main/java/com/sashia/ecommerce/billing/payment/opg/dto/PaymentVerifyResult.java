package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Result of verifying a payment at a gateway.
 */
public record PaymentVerifyResult(
        @NonNull PaymentGatewayType gatewayType,
        @NonNull PaymentStatus status,
        @NonNull String authority,
        @Nullable String referenceId,
        @Nullable String feeType,
        @Nullable Integer fee
) {
    public boolean isSuccessful() {
        return status == PaymentStatus.PAID;
    }
}
