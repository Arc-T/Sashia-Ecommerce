package com.sashia.ecommerce.promotion.internal;

import com.sashia.ecommerce.promotion.Promotion;
import com.sashia.ecommerce.promotion.PromotionService;
import com.sashia.ecommerce.promotion.engine.resolver.PromotionResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionResolver promotionResolver;

    public PromotionController(PromotionService promotionService, PromotionResolver promotionResolver) {
        this.promotionService = promotionService;
        this.promotionResolver = promotionResolver;
    }

    @GetMapping
//    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')") //TODO: assign authorize
    ResponseEntity<List<Promotion>> readAll() {
        return ResponseEntity.ok(promotionResolver.resolve());
    }

}
