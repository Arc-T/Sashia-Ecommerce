package com.sashia.ecommerce.media;

import com.sashia.ecommerce.media.dto.MediaCreateRequest;
import com.sashia.ecommerce.media.dto.MediaResponse;

import java.util.List;

public interface MediaService {

    List<Long> create(Long productId);

    void createDraft(MediaCreateRequest mediaCreateRequest);

    void createDraft(Long productId, MediaCreateRequest mediaCreateRequest);

    List<MediaResponse> readAllDraft();

    List<MediaResponse> readDraft(Long productId);

    void update(Long productId);

    void delete(Long id);

    void deleteDraft(String fileName);

    void deleteDraft(Long productId, String fileName);

}
