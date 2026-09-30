package com.sashia.ecommerce.ordering.delivery.internal;

import com.sashia.ecommerce.ordering.delivery.DeliveryService;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOptionRepository;
import com.sashia.ecommerce.promotion.engine.PromotionEngine;
import com.sashia.ecommerce.promotion.engine.dto.DeliveryPromotionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {

    private final PromotionEngine promotionEngine;
    private final DeliveryOptionRepository deliveryOptionRepository;

    public DeliveryServiceImpl(PromotionEngine promotionEngine, DeliveryOptionRepository deliveryOptionRepository) {
        this.promotionEngine = promotionEngine;
        this.deliveryOptionRepository = deliveryOptionRepository;
    }

    @Override
    public Optional<DeliveryDTO> read(Long id) {
        return deliveryOptionRepository.findById(id).map(DeliveryMapper::toDTO);
    }

    @Override
    public Page<DeliveryDTO> readAll(Pageable pageable) {

        List<DeliveryOption> deliveryOptions = deliveryOptionRepository.findAll(pageable).stream().toList();

        promotionEngine.apply(new DeliveryPromotionRequest(deliveryOptions));

        return new PageImpl<>(deliveryOptions.stream()
                .map(DeliveryMapper::toDTO)
                .toList(),
                pageable,
                deliveryOptions.size());
    }

}