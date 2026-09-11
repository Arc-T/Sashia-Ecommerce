package com.sashia.ecommerce.ordering.order.internal;

import com.sashia.ecommerce.catalog.item.ItemVariantDTO;
import com.sashia.ecommerce.catalog.item.dto.ItemDeliveryMethod;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.catalog.item.variant.ItemVariantRepository;
import com.sashia.ecommerce.identity.user.UserRepository;
import com.sashia.ecommerce.ordering.order.*;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderSearchDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.ecommerce.ordering.shipment.Shipment;
import com.sashia.ecommerce.ordering.shipment.ShipmentRepository;
import com.sashia.ecommerce.promotion.coupon.Coupon;
import com.sashia.ecommerce.promotion.engine.PromotionEngine;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;
import com.sashia.ecommerce.promotion.engine.dto.CartPromotionRequest;
import com.sashia.shared.exception.BusinessRuleException;
import com.sashia.shared.exception.ResourceNotFoundException;
import com.sashia.shared.util.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final PromotionEngine promotionEngine;
    private final ItemVariantRepository itemVariantRepository;
    private final ShipmentRepository shipmentRepository;

    public OrderServiceImpl(UserRepository userRepository, PromotionEngine promotionEngine, ItemVariantRepository itemVariantRepository, ShipmentRepository shipmentRepository) {
        this.userRepository = userRepository;
        this.promotionEngine = promotionEngine;
        this.itemVariantRepository = itemVariantRepository;
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    @Transactional
    public Long create(CheckoutRequest request) {
        long userId = SecurityUtils.getCurrentUserId();

        List<ItemVariant> itemVariants = new ArrayList<>(request.items().size());

        Coupon coupon = null; //TODO: full implementation needed

        Shipment shipment = shipmentRepository
                .findById(request.delivery().shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("shipping.method.invalid"));

        for (var cartItem : request.items()) {
            ItemVariantDTO cartItemVariant = cartItem.itemVariants().getFirst();

            ItemVariant itemVariant = itemVariantRepository
                    .findByIdAndItemId(cartItemVariant.id(), cartItem.id())
                    .orElseThrow(() -> new ResourceNotFoundException("item.not.found"));

            if (itemVariant.getStock() < cartItemVariant.quantity()) {
                throw new BusinessRuleException("checkout.item.stock.exceed");
            }

            itemVariant.setQuantity(cartItemVariant.quantity());

            itemVariants.add(itemVariant);
        }

        promotionEngine.apply(new CartPromotionRequest(coupon, shipment, itemVariants));

        Order order = new Order();

        BigDecimal orderSubtotal = BigDecimal.ZERO;
        BigDecimal orderDiscountAmount = BigDecimal.ZERO;
        BigDecimal orderTotal = BigDecimal.ZERO;

        for (ItemVariant itemVariant : itemVariants) {
            OrderDetails orderDetails = new OrderDetails();

            BigDecimal orderDetailsTotal = itemVariant.calculateTotal();
            BigDecimal orderDetailsSubTotal = itemVariant.calculateSubTotal();
            BigDecimal orderDetailsDiscountAmount = itemVariant.calculateTotalDiscountAmount();

            orderDetails.setChargeType(OrderChargeType.ITEM_VARIANT);
            orderDetails.setChargeTypeId(itemVariant.getId());
            orderDetails.setChargeTypeName(itemVariant.getItem().getTitle());
            orderDetails.setItemVariant(itemVariant);
            orderDetails.setUnitPrice(itemVariant.getUnitPrice());
            orderDetails.setQuantity(itemVariant.getQuantity());
            orderDetails.setSubtotal(orderDetailsSubTotal);
            orderDetails.setTaxAmount(BigDecimal.ZERO); //TODO: I really don't know what are these
            orderDetails.setTaxRate(BigDecimal.ZERO); //TODO: I really don't know what are these
            orderDetails.setTotalDiscountAmount(orderDetailsDiscountAmount);
            orderDetails.setTotal(orderDetailsSubTotal);

            if (itemVariant.hasPromotion()) {
                orderDetails.setPromotions(
                        itemVariant
                                .getAppliedPromotions()
                                .stream()
                                .map(AppliedPromotion::promotion)
                                .collect(Collectors.toUnmodifiableSet())
                );

                orderDiscountAmount = orderDiscountAmount.add(orderDetailsDiscountAmount);
            }

            orderTotal = orderTotal.add(orderDetailsTotal);
            orderSubtotal = orderSubtotal.add(orderDetailsSubTotal);

            order.getOrderDetails().add(orderDetails);
        }

        order.setUser(userRepository.getReferenceById(userId));
        order.setStatus(OrderStatusType.PENDING);
        order.setUserNote(request.description());
        order.setItemType(ItemType.PRODUCT); //TODO: dynamic type it should be
        order.setPricing(
                new PricingDetails(
                        CurrencyCode.IRR, //TODO: dynamic type it should be
                        orderSubtotal,
                        shipment.calculateTotal(),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO, //TODO: additional charges needed
                        orderDiscountAmount,
                        orderTotal
                )
        );
        order.setDelivery(
                new DeliveryDetails(
                        ItemDeliveryMethod.SHIPPING,
                        request.delivery().address(),
                        request.delivery().receiverName(),
                        request.delivery().receiverPhone(),
                        request.delivery().receiverEmail()
                )
        );

        return order.getId();
    }

    @Override
    public Optional<OrderDTO> get(Long id) {
        return Optional.empty();
    }

    @Override
    public Page<OrderDTO> getAll(Pageable pageable, OrderSearchDTO search) {
        //        return orderRepository.findAll(pageable, search).map(OrderDTO::toDTO);
         return null;
    }

    @Override
    public void update(Long id, OrderDTO order) {
    }
}
