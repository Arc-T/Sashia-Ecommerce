package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Result of initiating a payment at a gateway.
 * {@code redirectUrl} is where the client should send the user.
 * {@code authority} is the gateway transaction token (e.g. Zarinpal authority).
 */
public record PaymentInitiateResult(
        @NonNull PaymentGatewayType gatewayType,
        @NonNull String authority,
        @NonNull String redirectUrl,
        @Nullable String feeType,
        @Nullable Integer fee
) {
}
