package com.sashia.ecommerce.notification.dto;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record SMSEventDTO(
        @NonNull String phone,
        @NonNull SMSType type,
        @Nullable String... params
) {
}