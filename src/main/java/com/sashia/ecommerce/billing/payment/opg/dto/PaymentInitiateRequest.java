package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Provider-agnostic request to start a payment at a gateway.
 */
public record PaymentInitiateRequest(
        @NonNull PaymentGatewayType gatewayType,
        @NonNull Long orderId,
        @NonNull @Positive Long amount,
        @Nullable String description,
        @Nullable String callbackUrl,
        @Nullable String mobile,
        @Nullable String email
) {
}
