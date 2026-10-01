package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.Nullable;

/**
 * HTTP body for POST /payments/initiate.
 */
public record InitiatePaymentApiRequest(
        @NotNull PaymentGatewayType gatewayType,
        @NotNull Long orderId,
        @NotNull @Positive Long amount,
        @Nullable String description,
        @Nullable String callbackUrl,
        @Nullable String mobile,
        @Nullable String email
) {
}
