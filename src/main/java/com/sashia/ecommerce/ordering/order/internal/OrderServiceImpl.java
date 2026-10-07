package com.sashia.ecommerce.ordering.order.internal;

import com.sashia.ecommerce.catalog.item.ItemVariantResponse;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.catalog.item.variant.ItemVariantRepository;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.identity.user.UserRepository;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOptionRepository;
import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.ordering.order.OrderRepository;
import com.sashia.ecommerce.ordering.order.OrderService;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderSearchDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.ecommerce.ordering.order.status.OrderStatus;
import com.sashia.ecommerce.ordering.order.status.OrderStatusRepository;
import com.sashia.ecommerce.ordering.order.transaction.OrderTransaction;
import com.sashia.ecommerce.ordering.order.transaction.OrderTransactionRepository;
import com.sashia.ecommerce.promotion.coupon.Coupon;
import com.sashia.ecommerce.promotion.engine.PromotionEngine;
import com.sashia.ecommerce.promotion.engine.dto.CartPromotionRequest;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import com.sashia.shared.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PromotionEngine promotionEngine;
    private final ItemVariantRepository itemVariantRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final DeliveryOptionRepository deliveryOptionRepository;
    private final OrderTransactionRepository orderTransactionRepository;

    @Override
    @Transactional
    public Long create(CheckoutRequest request) {
        DeliveryOption deliveryOption = deliveryOptionRepository.findById(request.delivery().deliveryOptionId())
                .orElseThrow(() -> new ResourceNotFoundException("item.delivery.not.found"));

        List<ItemVariant> itemVariants = resolveItemVariants(request);
        User user = userRepository.getReferenceById(SecurityUtils.getCurrentUserId());

        promotionEngine.apply(new CartPromotionRequest(resolveCoupon(), deliveryOption, itemVariants));

        Order order = OrderMapper.toEntity(request, user, deliveryOption, itemVariants, generateOrderNumber());

        orderRepository.save(order);
        recordStatusTransition(order, OrderStatusType.PENDING, "Order created at checkout");
        reserveStock(itemVariants);

        return order.getId();
    }

    @Override
    @Transactional
    public void transitionStatus(Long orderId, OrderStatusType newStatus, String description) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("order.not.found"));

        OrderStatusType current = order.getStatus();
        if (current == newStatus) {
            return;
        }

        if (current == null || !current.canTransitionTo(newStatus)) {
            throw new BusinessRuleException("order.status.transition.invalid");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
        recordStatusTransition(order, newStatus, description);
    }

    /* =============================== RESOLUTION =============================== */

    private Coupon resolveCoupon() {
        return null; // TODO: full coupon resolution
    }

    private List<ItemVariant> resolveItemVariants(CheckoutRequest request) {
        List<ItemVariant> itemVariants = new ArrayList<>(request.items().size());

        for (var cartItem : request.items()) {
            ItemVariantResponse cartItemVariant = cartItem.itemVariants().getFirst();

            ItemVariant itemVariant = itemVariantRepository
                    .findByIdAndItemIdForUpdate(cartItemVariant.id(), cartItem.id())
                    .orElseThrow(() -> new ResourceNotFoundException("item.not.found"));

            if (itemVariant.getStock() < cartItemVariant.quantity()) {
                throw new BusinessRuleException("checkout.item.stock.exceed");
            }

            itemVariant.setQuantity(cartItemVariant.quantity());
            itemVariants.add(itemVariant);
        }

        return itemVariants;
    }

    /* =============================== STATUS =============================== */

    private void recordStatusTransition(Order order, OrderStatusType type, String description) {
        OrderStatus status = orderStatusRepository.findByType(type)
                .orElseThrow(() -> new IllegalStateException("Missing OrderStatus row for type: " + type));

        OrderTransaction transaction = new OrderTransaction();
        transaction.setOrder(order);
        transaction.setOrderStatus(status);
        transaction.setDescription(description);

        orderTransactionRepository.save(transaction);
    }

    /* =============================== PERSISTENCE SIDE-EFFECTS =============================== */

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void reserveStock(List<ItemVariant> itemVariants) {
        for (ItemVariant itemVariant : itemVariants) {
            itemVariant.setStock(itemVariant.getStock() - itemVariant.getQuantity());
        }
        itemVariantRepository.saveAll(itemVariants);
    }

    /* =========================== NOT YET IMPLEMENTED =========================== */

    @Override
    public Optional<OrderDTO> get(Long id) {
        return Optional.empty();
    }

    @Override
    public void update(Long id, OrderDTO order) {
    }

    @Override
    public Page<OrderDTO> getAll(Pageable pageable, OrderSearchDTO search) {
        return null;
    }
}