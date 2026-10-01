package com.sashia.ecommerce.billing.payment.opg.dto;

import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * HTTP body for POST /payments/verify (client-driven verify).
 * Gateway callback may also use query params — see PaymentController.
 */
public record VerifyPaymentApiRequest(
        @NotNull PaymentGatewayType gatewayType,
        @NotBlank String authority,
        @NotNull @Positive Long amount,
        @NotNull Long orderId
) {
}
