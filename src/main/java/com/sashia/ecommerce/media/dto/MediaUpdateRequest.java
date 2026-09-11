package com.sashia.ecommerce.media.dto;

import org.springframework.web.multipart.MultipartFile;

public record MediaUpdateRequest(MultipartFile[] media) {
}
