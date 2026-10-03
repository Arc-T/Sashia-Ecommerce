package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.Media;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long>, JpaSpecificationExecutor<Media> {

    Optional<Media> findBySlug(String slug);

    List<Media> findAllByResourceTypeAndResourceIdOrderByDisplayOrderAscIdAsc(MediaResourceType resourceType, Long resourceId);

    List<Media> findAllByResourceTypeAndResourceIdAndStatusOrderByDisplayOrderAscIdAsc(MediaResourceType resourceType,
                                                                                       Long resourceId,
                                                                                       MediaStatus status);

    List<Media> findAllByStatusAndCreatedAtBefore(MediaStatus status, LocalDateTime createdAt, Pageable pageable);

}
