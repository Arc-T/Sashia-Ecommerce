package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

/**
 * Response envelope from Zarinpal request / verify APIs.
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ZarinpalResponse(
        Data data,
        Object errors
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Data(
            Integer code,
            String message,
            String authority,
            String feeType,
            Integer fee,
            Long refId,
            String cardHash,
            String cardPan
    ) {
    }

    public boolean isSuccess() {
        return data != null && data.code() != null && data.code() == 100;
    }
}
