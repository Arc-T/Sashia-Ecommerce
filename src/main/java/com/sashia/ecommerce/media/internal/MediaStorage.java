package com.sashia.ecommerce.media.internal;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Where the bytes live. The only implementation is the local disk; an S3 / MinIO one can replace it
 * without touching the service.
 */
interface MediaStorage {

    record StoredFile(String sha256, long size) {
    }

    /** Writes atomically: readers never see a half written file. {@code key} must not exist yet. */
    StoredFile store(String key, InputStream content) throws IOException;

    /** @throws com.sashia.shared.exception.ResourceNotFoundException when the file is missing */
    Resource load(String key);

    /** Idempotent and never throws: a failure is logged and left for a later cleanup. */
    void delete(String key);

}
