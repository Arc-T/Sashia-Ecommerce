package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.jspecify.annotations.NonNull;

/**
 * Provider-agnostic request to verify a payment after the user returns from the gateway.
 */
public record PaymentVerifyRequest(
        @NonNull PaymentGatewayType gatewayType,
        @NotBlank String authority,
        @NonNull @Positive Long amount,
        @NonNull Long orderId
) {
}
