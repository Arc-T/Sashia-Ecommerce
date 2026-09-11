package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.MediaService;
import com.sashia.ecommerce.media.dto.MediaCreateRequest;
import com.sashia.ecommerce.media.dto.MediaResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // ================================ GET ================================

    @GetMapping("/draft")
    @PreAuthorize("hasAuthority('READ_ALL_DRAFT_MEDIA')")
    public ResponseEntity<List<MediaResponse>> readAllDraft() {
        return ResponseEntity.ok().body(mediaService.readAllDraft());
    }

    @GetMapping("/draft/{productId}")
    @PreAuthorize("hasAuthority('READ_ALL_DRAFT_MEDIA')")
    public ResponseEntity<List<MediaResponse>> readAllDraft(@PathVariable Long productId) {
        return ResponseEntity.ok().body(mediaService.readDraft(productId));
    }

//    @GetMapping("/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//        Resource file = storageService.loadAsResource(filename);
//        if (file == null)
//            return ResponseEntity.notFound().build();
//
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    // ================================ POST ================================

    @PostMapping(path = "/draft", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_DRAFT_MEDIA')")
    public ResponseEntity<?> createDraft(MediaCreateRequest MediaCreateRequest) {
        mediaService.createDraft(MediaCreateRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/draft/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CREATE_DRAFT_MEDIA')")
    public ResponseEntity<?> createDraft(@PathVariable Long productId, MediaCreateRequest MediaCreateRequest) {
        mediaService.createDraft(productId, MediaCreateRequest);
        return ResponseEntity.noContent().build();
    }

    // ================================ DELETE ================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_MEDIA')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/draft/{name}")
    @PreAuthorize("hasAuthority('DELETE_DRAFT_MEDIA')")
    public ResponseEntity<?> deleteDraft(@PathVariable String name) {
        mediaService.deleteDraft(name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/draft/{productId}/{name}")
    @PreAuthorize("hasAuthority('DELETE_DRAFT_MEDIA')")
    public ResponseEntity<?> deleteDraft(@PathVariable Long productId, @PathVariable String name) {
        mediaService.deleteDraft(productId, name);
        return ResponseEntity.noContent().build();
    }

}
