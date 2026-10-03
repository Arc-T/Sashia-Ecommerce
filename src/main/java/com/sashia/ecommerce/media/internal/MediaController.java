package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaService;
import com.sashia.ecommerce.media.dto.*;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.Duration;
import java.util.List;

/**
 * <pre>
 * POST   /media/drafts                          upload files as drafts (multipart)
 * GET    /media/drafts                          my drafts
 * GET    /media                                 search everything                 (manager)
 * GET    /media/{id}                            one media
 * PUT    /media/{id}                            change description / order
 * DELETE /media/{id}                            delete
 * GET    /media/resources/{type}/{id}           saved media of a resource         (public)
 * PUT    /media/resources/{type}/{id}           set the exact ordered media list  (manager)
 * GET    /media/files/{slug}                    the file itself                   (public if saved)
 * </pre>
 */
@RestController
@RequestMapping(path = "/media")
class MediaController {

    private final MediaService mediaService;

    MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // ================================ FILES ================================

    @GetMapping("/files/{slug:.+}")
    ResponseEntity<Resource> file(@PathVariable String slug, WebRequest request) {
        MediaContent content = mediaService.loadContent(slug);

        String etag = "\"" + content.checksum() + "\"";
        if (request.checkNotModified(etag))
            return null; // 304 already written

        // saved files never change (the slug is unique), so they can be cached "forever"
        CacheControl cache = content.publicContent()
                ? CacheControl.maxAge(Duration.ofDays(365)).cachePublic().immutable()
                : CacheControl.noStore().cachePrivate();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(content.mimeType()))
                .contentLength(content.size())
                .eTag(etag)
                .cacheControl(cache)
                .header("X-Content-Type-Options", "nosniff")
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline().filename(content.fileName()).build().toString())
                .body(content.resource());
    }

    // ================================ GET ================================

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_MEDIA')")
    ResponseEntity<Page<MediaResponse>> readAll(Pageable pageable, MediaSearchRequest search) {
        return ResponseEntity.ok(mediaService.readAll(pageable, search));
    }

    @GetMapping("/drafts")
    @PreAuthorize("hasAuthority('CREATE_MEDIA')")
    ResponseEntity<List<MediaResponse>> readOwnDrafts(@RequestParam(required = false) MediaResourceType resourceType,
                                                      @RequestParam(required = false) Long resourceId) {
        return ResponseEntity.ok(mediaService.readOwnDrafts(resourceType, resourceId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READ_ALL_MEDIA', 'CREATE_MEDIA')")
    ResponseEntity<MediaResponse> read(@PathVariable Long id) {
        return ResponseEntity.of(mediaService.read(id));
    }

    @GetMapping("/resources/{resourceType}/{resourceId}")
    ResponseEntity<List<MediaResponse>> readByResource(@PathVariable MediaResourceType resourceType,
                                                       @PathVariable Long resourceId) {
        return ResponseEntity.ok(mediaService.readByResource(resourceType, resourceId));
    }

    // ================================ POST ================================

    @PostMapping(path = "/drafts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_MEDIA')")
    ResponseEntity<List<MediaResponse>> uploadDrafts(@Valid MediaUploadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaService.uploadDrafts(request));
    }

    // ================================ PUT ================================

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CREATE_MEDIA', 'UPDATE_MEDIA')")
    ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody MediaUpdateRequest request) {
        mediaService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/resources/{resourceType}/{resourceId}")
    @PreAuthorize("hasAuthority('UPDATE_MEDIA')")
    ResponseEntity<List<MediaResponse>> sync(@PathVariable MediaResourceType resourceType,
                                             @PathVariable Long resourceId,
                                             @Valid @RequestBody MediaSyncRequest request) {
        return ResponseEntity.ok(mediaService.sync(resourceType, resourceId, request.mediaIds()));
    }

    // ================================ DELETE ================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('CREATE_MEDIA', 'DELETE_MEDIA')")
    ResponseEntity<?> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
