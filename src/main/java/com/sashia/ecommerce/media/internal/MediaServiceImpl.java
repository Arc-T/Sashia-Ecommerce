package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaService;
import com.sashia.ecommerce.media.MediaStatus;
import com.sashia.ecommerce.media.dto.*;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.InvalidResourceException;
import com.sashia.shared.exception.ResourceNotFoundException;
import com.sashia.shared.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class MediaServiceImpl implements MediaService {

    static final String UPDATE = "UPDATE_MEDIA";
    static final String DELETE = "DELETE_MEDIA";
    static final String READ_ALL = "READ_ALL_MEDIA";

    private static final int MAX_ORIGINAL_NAME = 100;

    private final MediaRepository mediaRepository;
    private final MediaStorage storage;
    private final MediaInspector inspector;
    private final MediaProperties properties;

    public MediaServiceImpl(MediaRepository mediaRepository, MediaStorage storage,
                            MediaInspector inspector, MediaProperties properties) {
        this.mediaRepository = mediaRepository;
        this.storage = storage;
        this.inspector = inspector;
        this.properties = properties;
    }

    // ================================ UPLOAD ================================

    @Override
    @Transactional
    public List<MediaResponse> uploadDrafts(MediaUploadRequest request) {
        MultipartFile[] files = request.files();
        if (files == null || files.length == 0)
            throw new InvalidResourceException("media.files.required");
        if (files.length > properties.maxFilesPerRequest())
            throw new InvalidResourceException("media.files.too.many");
        if ((request.resourceType() == null) != (request.resourceId() == null))
            throw new InvalidResourceException("media.resource.invalid");

        // Validate every file first: a bad file must not leave the good ones behind.
        List<MediaInspector.Inspection> inspections = new ArrayList<>(files.length);
        for (MultipartFile file : files)
            inspections.add(inspector.inspect(file));

        Long ownerId = SecurityUtils.getCurrentUserId();
        List<String> storedKeys = new ArrayList<>(files.length);
        deleteFilesIfRolledBack(storedKeys);

        List<Media> created = new ArrayList<>(files.length);
        for (int i = 0; i < files.length; i++) {
            MediaFormat format = inspections.get(i).format();
            String slug = UUID.randomUUID() + "." + format.extension();
            String key = storageKey(slug);

            MediaStorage.StoredFile stored;
            try (InputStream in = files[i].getInputStream()) {
                stored = storage.store(key, in);
            } catch (IOException e) {
                throw new UncheckedIOException("MEDIA_STORE_FAILED", e);
            }
            storedKeys.add(key);

            Media media = new Media();
            media.setSlug(slug);
            media.setStorageKey(key);
            media.setOriginalName(sanitizeName(files[i].getOriginalFilename()));
            media.setChecksum(stored.sha256());
            media.setSize(stored.size());
            media.setMimeType(format.mimeType());
            media.setExtension(format.extension());
            media.setMetadata(inspections.get(i).metadata());
            media.setStatus(MediaStatus.DRAFT);
            media.setOwnerId(ownerId);
            media.setResourceType(request.resourceType());
            media.setResourceId(request.resourceId());
            media.setDescription(request.description());
            media.setDisplayOrder(i);
            created.add(media);
        }

        return mediaRepository.saveAll(created).stream().map(MediaMapper::toDTO).toList();
    }

    // ================================ READ ================================

    @Override
    public Page<MediaResponse> readAll(Pageable pageable, MediaSearchRequest search) {
        return mediaRepository.findAll(MediaSpecification.bySearch(search), pageable).map(MediaMapper::toDTO);
    }

    @Override
    public List<MediaResponse> readOwnDrafts(MediaResourceType resourceType, Long resourceId) {
        Specification<Media> spec = MediaSpecification.hasStatus(MediaStatus.DRAFT)
                .and(MediaSpecification.hasOwner(SecurityUtils.getCurrentUserId()))
                .and(MediaSpecification.hasResource(resourceType, resourceId));

        return mediaRepository.findAll(spec, Sort.by("createdAt").descending().and(Sort.by("id").descending()))
                .stream().map(MediaMapper::toDTO).toList();
    }

    @Override
    public Optional<MediaResponse> read(Long id) {
        return mediaRepository.findById(id)
                .filter(this::canSee)
                .map(MediaMapper::toDTO);
    }

    @Override
    public List<MediaResponse> readByResource(MediaResourceType resourceType, Long resourceId) {
        return mediaRepository
                .findAllByResourceTypeAndResourceIdAndStatusOrderByDisplayOrderAscIdAsc(resourceType, resourceId, MediaStatus.SAVED)
                .stream().map(MediaMapper::toDTO).toList();
    }

    @Override
    public MediaContent loadContent(String slug) {
        Media media = mediaRepository.findBySlug(slug)
                .filter(this::canSee)
                .orElseThrow(() -> new ResourceNotFoundException("media.not.found"));

        return new MediaContent(
                storage.load(media.getStorageKey()),
                media.getSlug(),
                media.getMimeType(),
                media.getSize(),
                media.getChecksum(),
                !media.isDraft()
        );
    }

    // ================================ WRITE ================================

    @Override
    @Transactional
    public void update(Long id, MediaUpdateRequest request) {
        Media media = findOrThrow(id);
        requireCanModify(media, UPDATE);

        if (request.description() != null)
            media.setDescription(request.description());
        if (request.displayOrder() != null)
            media.setDisplayOrder(request.displayOrder());
    }

    @Override
    @Transactional
    public List<MediaResponse> sync(MediaResourceType resourceType, Long resourceId, List<Long> mediaIds) {
        if (resourceType == null || resourceId == null)
            throw new InvalidResourceException("media.resource.invalid");

        List<Long> ids = mediaIds == null ? List.of() : mediaIds;
        if (new HashSet<>(ids).size() != ids.size())
            throw new InvalidResourceException("media.ids.duplicated");

        Map<Long, Media> requested = new HashMap<>();
        mediaRepository.findAllById(ids).forEach(m -> requested.put(m.getId(), m));

        // 1. validate everything, change nothing yet
        for (Long id : ids) {
            Media media = requested.get(id);
            if (media == null)
                throw new ResourceNotFoundException("media.not.found");

            if (media.isDraft()) {
                if (!isOwnerOrManager(media, UPDATE))
                    throw new ResourceNotFoundException("media.not.found"); // do not reveal other users' drafts
                if (media.getResourceType() != null && !media.belongsTo(resourceType, resourceId))
                    throw new BusinessRuleException("media.resource.mismatch");
            } else if (!media.belongsTo(resourceType, resourceId)) {
                throw new BusinessRuleException("media.already.attached");
            }
        }

        // 2. apply the order and publish drafts
        for (int i = 0; i < ids.size(); i++) {
            Media media = requested.get(ids.get(i));
            media.setStatus(MediaStatus.SAVED);
            media.setResourceType(resourceType);
            media.setResourceId(resourceId);
            media.setDisplayOrder(i);
        }

        // 3. saved media of this resource that are no longer listed are removed
        List<Media> removed = mediaRepository
                .findAllByResourceTypeAndResourceIdAndStatusOrderByDisplayOrderAscIdAsc(resourceType, resourceId, MediaStatus.SAVED)
                .stream()
                .filter(m -> !requested.containsKey(m.getId()))
                .toList();
        deleteAll(removed);

        return ids.stream().map(requested::get).map(MediaMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Media media = findOrThrow(id);
        requireCanModify(media, DELETE);
        deleteAll(List.of(media));
    }

    @Override
    @Transactional
    public void deleteByResource(MediaResourceType resourceType, Long resourceId) {
        deleteAll(mediaRepository.findAllByResourceTypeAndResourceIdOrderByDisplayOrderAscIdAsc(resourceType, resourceId));
    }

    // ================================ INTERNAL (used by the cleanup job) ================================

    /** Deletes the rows now and the files right after the transaction commits. */
    @Transactional
    public void deleteAll(Collection<Media> medias) {
        if (medias.isEmpty())
            return;
        List<String> keys = medias.stream().map(Media::getStorageKey).toList();
        mediaRepository.deleteAll(medias);
        deleteFilesAfterCommit(keys);
    }

    // ================================ ACCESS RULES ================================

    /** Saved media is public. A draft is visible to its owner and to managers only. */
    private boolean canSee(Media media) {
        return !media.isDraft() || isOwnerOrManager(media, READ_ALL);
    }

    /** Drafts: owner or holder of {@code managerAuthority}. Saved media: only the holder of {@code managerAuthority}. */
    private void requireCanModify(Media media, String managerAuthority) {
        boolean allowed = media.isDraft()
                ? isOwnerOrManager(media, managerAuthority)
                : hasAuthority(managerAuthority);

        if (allowed)
            return;
        if (canSee(media))
            throw new AuthorizationDeniedException("media.forbidden", new AuthorizationDecision(false));
        throw new ResourceNotFoundException("media.not.found");
    }

    private boolean isOwnerOrManager(Media media, String managerAuthority) {
        if (!SecurityUtils.isAuthenticated())
            return false;
        return hasAuthority(managerAuthority)
                || (media.getOwnerId() != null && media.getOwnerId().equals(SecurityUtils.getCurrentUserId()));
    }

    private boolean hasAuthority(String authority) {
        return SecurityUtils.isAuthenticated() && SecurityUtils.hasAuthority(authority);
    }

    // ================================ HELPERS ================================

    private Media findOrThrow(Long id) {
        return mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("media.not.found"));
    }

    /** yyyy/MM keeps directories small. */
    private static String storageKey(String slug) {
        LocalDate today = LocalDate.now();
        return "%d/%02d/%s".formatted(today.getYear(), today.getMonthValue(), slug);
    }

    /** Keep the display name only: no path, no control characters, bounded length. */
    static String sanitizeName(String name) {
        if (name == null)
            return null;
        String cleaned = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1)
                .replaceAll("\\p{Cntrl}", "")
                .strip();
        if (cleaned.isEmpty())
            return null;
        return cleaned.length() > MAX_ORIGINAL_NAME ? cleaned.substring(0, MAX_ORIGINAL_NAME) : cleaned;
    }

    /** Files written during this transaction are removed if it does not commit. */
    private void deleteFilesIfRolledBack(List<String> keys) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED)
                    keys.forEach(storage::delete);
            }
        });
    }

    /** Files are removed only after the rows are really gone, so a rollback never loses a file. */
    private void deleteFilesAfterCommit(List<String> keys) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            keys.forEach(storage::delete);
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                keys.forEach(storage::delete);
            }
        });
    }

}
