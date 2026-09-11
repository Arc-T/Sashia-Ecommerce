package com.sashia.ecommerce.promotion.engine.pipeline.handler.target;

import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;

import java.util.LinkedHashSet;
import java.util.Set;

public record TargetMatchResult(
        boolean applicable,
        Set<ItemSummaryDTO> affectedItems) {

    public TargetMatchResult(boolean matched) {
        this(matched, new LinkedHashSet<>());
    }

}