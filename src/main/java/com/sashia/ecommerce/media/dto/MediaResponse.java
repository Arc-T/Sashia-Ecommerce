package com.sashia.ecommerce.media.dto;

public record MediaResponse(String url, String extension) {

    public MediaResponse(String url) {
        this(url, null);
    }

}