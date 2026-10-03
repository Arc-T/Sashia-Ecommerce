package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaStatus;
import com.sashia.shared.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Media Draft Cleanup Job Tests")
class MediaDraftCleanupJobTest extends BaseControllerTest {

    @Autowired
    private MediaDraftCleanupJob job;

    @Autowired
    private MediaRepository mediaRepository;

    @BeforeEach
    void cleanUp() {
        mediaRepository.deleteAll();
    }

    private Media media(String slug, MediaStatus status) {
        Media media = new Media();
        media.setSlug(slug);
        media.setStorageKey("2026/01/" + slug);
        media.setChecksum("0".repeat(64));
        media.setSize(1L);
        media.setMimeType("image/png");
        media.setExtension("png");
        media.setStatus(status);
        media.setOwnerId(1L);
        if (status == MediaStatus.SAVED) {
            media.setResourceType(MediaResourceType.PRODUCT);
            media.setResourceId(1L);
        }
        return mediaRepository.save(media);
    }

    @Test
    @DisplayName("Should delete expired drafts and keep saved media")
    void shouldDeleteOnlyExpiredDrafts() {
        Media draft = media("draft.png", MediaStatus.DRAFT);
        Media saved = media("saved.png", MediaStatus.SAVED);

        int removed = job.purgeDraftsOlderThan(LocalDateTime.now().plusDays(1));

        assertThat(removed).isEqualTo(1);
        assertThat(mediaRepository.findById(draft.getId())).isEmpty();
        assertThat(mediaRepository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("Should keep drafts that are not old enough")
    void shouldKeepFreshDrafts() {
        Media draft = media("fresh.png", MediaStatus.DRAFT);

        int removed = job.purgeDraftsOlderThan(LocalDateTime.now().minusDays(1));

        assertThat(removed).isZero();
        assertThat(mediaRepository.findById(draft.getId())).isPresent();
    }

    @Test
    @DisplayName("Should work through more than one batch")
    void shouldPurgeInBatches() {
        for (int i = 0; i < 230; i++)
            media("bulk-" + i + ".png", MediaStatus.DRAFT);

        int removed = job.purgeDraftsOlderThan(LocalDateTime.now().plusDays(1));

        assertThat(removed).isEqualTo(230);
        assertThat(mediaRepository.count()).isZero();
    }

}
