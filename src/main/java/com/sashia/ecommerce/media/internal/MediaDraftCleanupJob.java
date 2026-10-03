package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.MediaStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Drafts that were never saved (the user closed the form) are deleted after {@code sashia.media.draft-ttl}.
 */
@Component
class MediaDraftCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(MediaDraftCleanupJob.class);
    private static final int BATCH_SIZE = 100;

    private final MediaRepository mediaRepository;
    private final MediaServiceImpl mediaService;
    private final MediaProperties properties;

    MediaDraftCleanupJob(MediaRepository mediaRepository, MediaServiceImpl mediaService, MediaProperties properties) {
        this.mediaRepository = mediaRepository;
        this.mediaService = mediaService;
        this.properties = properties;
    }

    @Scheduled(initialDelayString = "PT5M", fixedDelayString = "${sashia.media.cleanup-interval:PT1H}")
    void run() {
        int removed = purgeDraftsOlderThan(LocalDateTime.now().minus(properties.draftTtl()));
        if (removed > 0)
            log.info("MEDIA_DRAFTS_PURGED: {}", removed);
    }

    /** Each batch is its own transaction (the service method), so a failure only affects that batch. */
    int purgeDraftsOlderThan(LocalDateTime cutoff) {
        int total = 0;
        List<Media> batch;
        do {
            batch = mediaRepository.findAllByStatusAndCreatedAtBefore(MediaStatus.DRAFT, cutoff, PageRequest.of(0, BATCH_SIZE));
            mediaService.deleteAll(batch);
            total += batch.size();
        } while (batch.size() == BATCH_SIZE);
        return total;
    }

}
