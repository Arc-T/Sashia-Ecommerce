package com.sashia.ecommerce.promotion.engine.context;

import com.sashia.ecommerce.pricing.Priceable;

import java.util.Map;

public record PromotionCandidates(
        Map<Long, Priceable> candidates) {

    public void clearCandidateItems() {
        candidates.clear();
    }

    public boolean hasCandidate() {
        return !candidates.isEmpty();
    }

    public void keepCandidates(Map<Long, ? extends Priceable> candidates) {
        clearCandidateItems();
        this.candidates.putAll(candidates);
    }

}