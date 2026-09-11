package com.sashia.ecommerce.ordering.reservation;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation", schema = "ordering")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    private Long userId;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Long productId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime expiresAt;

    @Transient
    public boolean isActive() {
        return this.status == ReservationStatus.ACTIVE
                && LocalDateTime.now().isBefore(this.expiresAt);
    }

    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }

//    @Transient
//    public void release() {
//        if (this.status != ReservationStatus.ACTIVE) {
//            throw new IllegalStateException("Only active reservations can be released");
//        }
//        this.status = ReservationStatus.RELEASED;
//        this.releasedAt = LocalDateTime.now();
//    }
//
//    @Transient
//    public void markExpired() {
//        if (this.status == ReservationStatus.ACTIVE && this.isExpired()) {
//            this.status = ReservationStatus.EXPIRED;
//            this.releasedAt = LocalDateTime.now();
//        }
//    }

}
