package com.sashia.ecommerce.ordering.order.dto;

import java.util.*;

public enum OrderStatusType {

    PENDING,
    CANCELED,
    PAID,
    REJECTED,
    REFUND,
    APPROVED,
    PROCESSING,
    FINISHED;

    private static final Map<OrderStatusType, Set<OrderStatusType>> ALLOWED_TRANSITIONS;

    static {
        Map<OrderStatusType, Set<OrderStatusType>> map = new EnumMap<>(OrderStatusType.class);

        map.put(PENDING, EnumSet.of(PAID, CANCELED, REJECTED));
        map.put(PAID, EnumSet.of(APPROVED, PROCESSING, CANCELED, REFUND));
        map.put(APPROVED, EnumSet.of(PROCESSING, CANCELED));
        map.put(PROCESSING, EnumSet.of(FINISHED, CANCELED));
        map.put(FINISHED, EnumSet.of(REFUND));

        ALLOWED_TRANSITIONS = Collections.unmodifiableMap(map);
    }

    /**
     * Whether this status may move directly to {@code target}.
     */
    public boolean canTransitionTo(OrderStatusType target) {
        if (target == null) {
            return false;
        }
        if (this == target) {
            return true; // no-op / idempotent
        }
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(target);
    }

    /**
     * Allowed next statuses from this state (immutable).
     */
    public Set<OrderStatusType> allowedTransitions() {
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of());
    }

    public boolean isTerminal() {
        return allowedTransitions().isEmpty();
    }

    public boolean isPaid() {
        return this == PAID;
    }

    public boolean isPending() {
        return this == PENDING;
    }
}