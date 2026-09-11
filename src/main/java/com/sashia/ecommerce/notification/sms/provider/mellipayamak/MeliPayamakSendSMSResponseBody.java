package com.sashia.ecommerce.notification.sms.provider.mellipayamak;

public record MeliPayamakSendSMSResponseBody(
        String Value,
        Long RetStatus,
        String StrRetStatus
) {
}
