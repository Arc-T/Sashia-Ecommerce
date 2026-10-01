package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Map;

/**
 * Body sent to Zarinpal payment/request API.
 * Field names follow Zarinpal's snake_case contract.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ZarinpalRequest(
        String merchantId,
        Integer amount,
        String currency,
        String description,
        String callbackUrl,
        String referrerId,
        Map<String, String> metadata
) {
}
