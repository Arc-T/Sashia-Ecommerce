package com.sashia.ecommerce.promotion.engine.context;

import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.ordering.shipment.Shipment;
import com.sashia.ecommerce.pricing.Priceable;
import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.coupon.Coupon;
import com.sashia.ecommerce.promotion.engine.dto.CartPromotionRequest;
import com.sashia.ecommerce.promotion.engine.dto.ItemPromotionRequest;
import com.sashia.ecommerce.promotion.engine.dto.PromotionRequest;
import com.sashia.ecommerce.promotion.engine.dto.ShipmentPromotionRequest;
import com.sashia.ecommerce.promotion.engine.effect.PromotionEffect;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PromotionContext {

    /**
     * The promotion currently being evaluated.
     */
    private final Promotion promotion;

    /**
     * Original request passed into the promotion engine.
     */
    private final PromotionRequest request;

    /**
     * Candidate that are still eligible for the current promotion.
     *
     * <p>The candidate list is initialized with every requested and is
     * progressively narrowed by target matchers and condition evaluators.
     */
    private final Map<PromotableType, PromotionCandidates> candidates = new LinkedHashMap<>();

    /**
     * Effects produced while executing the promotion strategy.
     *
     * <p>These effects are later consumed by the {@code PromotionEffectApplier}
     * to update the final {@code PromotionResult}.
     */
    private final List<PromotionEffect> effects = new ArrayList<>();

    public PromotionContext(Promotion promotion, PromotionRequest request) {
        this.promotion = promotion;
        this.request = request;
        initialCandidateMaps();
    }

    /* *********************************** HELPERS ************************************ */

    public boolean isCandidatesEmpty() {
        return candidates.values().stream()
                .noneMatch(PromotionCandidates::hasCandidate);
    }

    private void initialCandidateMaps() {
        if (request instanceof CartPromotionRequest(Coupon _, Shipment shipment, List<ItemVariant> itemVariants)) {
            candidates.put(PromotableType.ITEM_VARIANT,
                    new PromotionCandidates(
                            itemVariants
                                    .stream()
                                    .collect(
                                            Collectors.toMap(ItemVariant::getId,
                                                    Function.identity())
                                    )
                    )
            );

            candidates.put(PromotableType.SHIPPING,
                    new PromotionCandidates(
                            Map.of(shipment.getId(), shipment)
                    )
            );

        } else if (request instanceof ItemPromotionRequest(List<ItemVariant> itemVariants)) {
            candidates.put(PromotableType.ITEM_VARIANT,
                    new PromotionCandidates(
                            itemVariants
                                    .stream()
                                    .collect(
                                            Collectors.toMap(ItemVariant::getId,
                                                    Function.identity())
                                    )
                    )
            );
        } else if (request instanceof ShipmentPromotionRequest(List<Shipment> shipments)) {
            candidates.put(PromotableType.SHIPPING, new PromotionCandidates(
                            shipments
                                    .stream()
                                    .collect(
                                            Collectors.toMap(Shipment::getId,
                                                    Function.identity())
                                    )
                    )
            );
        }
    }

    public List<PromotionEffect> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    public void addEffect(PromotionEffect effect) {
        effects.add(effect);
    }

    public Map<Long, Priceable> getCandidateItems(PromotableType promotableType) {
        return candidates.get(promotableType)
                .candidates();
    }

    public void clearAndAddCandidates(PromotableType promotableType, Map<Long, ? extends Priceable> candidates) {
        this.candidates.get(promotableType)
                .keepCandidates(candidates);
    }

    /* *********************************** GETTERS *********************************** */

    public Promotion getPromotion() {
        return promotion;
    }

    public PromotionRequest getRequest() {
        return request;
    }

    public Map<PromotableType, PromotionCandidates> getCandidates() {
        return candidates;
    }

}