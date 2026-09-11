package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import java.util.Map;

public record ZarinpalRequest(
        String merchantId,
        Integer amount,
        String concurrency, //Optional IRR/IRT
        String description,
        String callBackUrl,
        String referrerId, //Optional
        Map<String, String> metaData //Optional mboile:293i4, emial:adfiaef, order_id

) {
}

//'{
//        "merchant_id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx",
//        "amount": "1100",
//        "callback_url": "http://example.com/verify",
//        "referrer_id": "xxxx",
//        "description": "Transaction description.",
//        "metadata": {
//        "mobile": "09121234567",
//        "email": "info.test@example.com"
//        }
//}',