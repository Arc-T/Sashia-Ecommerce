package com.sashia.ecommerce.notification.dto;

import java.time.LocalDateTime;

public record SMSDTO(
        Long id,
        String phone,
        Long statusId,
        Long templateId,
        String text,
        String response,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public SMSDTO(String phone, Long statusId, Long templateId, String text, String response, String description) {
        this(
                null,
                phone,
                statusId,
                templateId,
                text,
                response,
                description,
                null,
                null
        );
    }

}
