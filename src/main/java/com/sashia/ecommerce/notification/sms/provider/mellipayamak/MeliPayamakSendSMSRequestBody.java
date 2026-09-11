package com.sashia.ecommerce.notification.sms.provider.mellipayamak;

public record MeliPayamakSendSMSRequestBody(
        String username,
        String password,
        String from,
        String phone,
        String message,
        boolean isFlash
) {
}
