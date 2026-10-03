package com.sashia.ecommerce.media;

import com.sashia.ecommerce.media.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Public API of the media module.
 *
 * <p>Typical flow for other modules (e.g. product):</p>
 * <ol>
 *     <li>the client uploads files with {@link #uploadDrafts(MediaUploadRequest)} and receives media ids;</li>
 *     <li>the client sends those ids together with the product form;</li>
 *     <li>the product service calls {@link #sync(MediaResourceType, Long, List)} to make them permanent.</li>
 * </ol>
 */
public interface MediaService {

    // ================================ UPLOAD ================================

    /** Stores the files as drafts owned by the current user. All files are validated before any is stored. */
    List<MediaResponse> uploadDrafts(MediaUploadRequest request);

    // ================================ READ ================================

    /** Search over everything (all owners, all statuses). */
    Page<MediaResponse> readAll(Pageable pageable, MediaSearchRequest search);

    /** Drafts of the current user, optionally limited to one resource. */
    List<MediaResponse> readOwnDrafts(MediaResourceType resourceType, Long resourceId);

    /** Empty when the media does not exist or the current user is not allowed to see it. */
    Optional<MediaResponse> read(Long id);

    /** Saved media of a resource, ordered. Public data. */
    List<MediaResponse> readByResource(MediaResourceType resourceType, Long resourceId);

    /** Bytes + headers data for the file endpoint. Drafts are only visible to their owner or a manager. */
    MediaContent loadContent(String slug);

    // ================================ WRITE ================================

    void update(Long id, MediaUpdateRequest request);

    /**
     * Makes {@code mediaIds} the exact, ordered media list of the resource:
     * listed drafts become SAVED, saved media that are not listed are deleted, order follows the list.
     * Authorization of the caller over the <em>resource</em> is the caller's responsibility.
     */
    List<MediaResponse> sync(MediaResourceType resourceType, Long resourceId, List<Long> mediaIds);

    void delete(Long id);

    /** Deletes every media of a resource (e.g. when a product is removed). */
    void deleteByResource(MediaResourceType resourceType, Long resourceId);

}
