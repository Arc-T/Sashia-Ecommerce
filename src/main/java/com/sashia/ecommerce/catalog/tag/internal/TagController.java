package com.sashia.ecommerce.catalog.tag.internal;

import com.sashia.ecommerce.catalog.tag.TagService;
import com.sashia.ecommerce.catalog.tag.dto.TagCreateRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import com.sashia.ecommerce.catalog.tag.dto.TagSearchRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/tags")
class TagController {

    private final TagService tagService;

    TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_TAGS')")
    ResponseEntity<Page<TagResponse>> readAll(TagSearchRequest search, Pageable pageable) {
        return ResponseEntity.ok(tagService.readAll(search, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_TAG')")
    ResponseEntity<TagResponse> read(@PathVariable Long id) {
        return ResponseEntity.of(tagService.read(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_TAG')")
    ResponseEntity<TagResponse> create(@RequestBody @Valid TagCreateRequest category) {
        return ResponseEntity.created(URI.create("/tags/" + tagService.create(category)))
                .build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_TAG')")
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid TagUpdateRequest category) {
        tagService.update(id, category);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_TAG')")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
