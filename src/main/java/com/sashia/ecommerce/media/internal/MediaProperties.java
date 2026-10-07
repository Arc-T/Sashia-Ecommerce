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
        @DefaultValue("uploads") Path root,
        @DefaultValue("20MB") DataSize maxFileSize,
        @DefaultValue("10") int maxFilesPerRequest,
        @DefaultValue("40000000") long maxPixels
) {
}
