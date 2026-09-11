package com.sashia.ecommerce.notification.sms.common;

public record SMSWrapperResponseDTO(
        Long statusId,
        String result,
        String description
) {
}