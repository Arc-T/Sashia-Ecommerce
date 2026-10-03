package com.sashia.ecommerce.media.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.unit.DataSize;

import java.nio.file.Path;
import java.time.Duration;

/**
 * {@code sashia.media.*}. Every value has a default, nothing is required in the yaml.
 */
@ConfigurationProperties(prefix = "sashia.media")
public record MediaProperties(
        /* storage directory. Use an absolute path in production. */
        @DefaultValue("uploads") Path root,
        /* keep <= spring.servlet.multipart.max-file-size */
        @DefaultValue("20MB") DataSize maxFileSize,
        @DefaultValue("10") int maxFilesPerRequest,
        /* drafts older than this are deleted by the cleanup job */
        @DefaultValue("24h") Duration draftTtl,
        /* rejects decompression bombs: width * height above this is refused */
        @DefaultValue("40000000") long maxPixels
) {
}
