package com.sashia.ecommerce.ordering.order.internal;

import com.sashia.ecommerce.catalog.item.dto.ItemType;
import com.sashia.ecommerce.catalog.item.variant.ItemVariant;
import com.sashia.ecommerce.identity.user.User;
import com.sashia.ecommerce.ordering.delivery.DeliveryDetails;
import com.sashia.ecommerce.ordering.delivery.option.DeliveryOption;
import com.sashia.ecommerce.ordering.order.*;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderStatusType;
import com.sashia.ecommerce.promotion.engine.dto.AppliedPromotion;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static Order toEntity(CheckoutRequest checkoutRequest, User user, DeliveryOption deliveryOption,
                                 List<ItemVariant> itemVariants, String orderNumber) {

        Order order = new Order();
        Set<OrderDetails> orderDetails = toOrderDetails(order, itemVariants);

        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setItemType(ItemType.PRODUCT); //TODO: derive from cart, not hardcoded
        order.setOrderDetails(orderDetails);
        order.setStatus(OrderStatusType.PENDING);
        order.setDeliveryOption(deliveryOption);
        order.setUserNote(checkoutRequest.description());
        order.setPricing(toPricingDetails(deliveryOption, orderDetails));
        order.setDelivery(toDeliveryDetails(checkoutRequest, deliveryOption));

        return order;
    }

    private static DeliveryDetails toDeliveryDetails(CheckoutRequest request, DeliveryOption deliveryOption) {
        return new DeliveryDetails(
                deliveryOption.getCode(), //TODO: derive from delivery option, not hardcoded
                request.delivery().address(),
                request.delivery().receiverName(),
                request.delivery().receiverPhone(),
                request.delivery().receiverEmail()
        );
    }

    private static OrderDetails toOrderDetail(Order order, ItemVariant itemVariant) {
        BigDecimal subtotal = itemVariant.calculateSubTotal();
        BigDecimal discountAmount = itemVariant.calculateTotalDiscountAmount();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder(order);
        orderDetails.setChargeType(OrderChargeType.ITEM_VARIANT);
        orderDetails.setChargeTypeId(itemVariant.getId());
        orderDetails.setChargeTypeName(itemVariant.getItem().getTitle());
        orderDetails.setItemVariant(itemVariant);
        orderDetails.setUnitPrice(itemVariant.getUnitPrice());
        orderDetails.setQuantity(itemVariant.getQuantity());
        orderDetails.setSubtotal(subtotal);
        orderDetails.setTaxAmount(BigDecimal.ZERO); //TODO
        orderDetails.setTaxRate(BigDecimal.ZERO); //TODO
        orderDetails.setTotalDiscountAmount(discountAmount);
        orderDetails.setTotal(subtotal.subtract(discountAmount));

        if (itemVariant.hasPromotion()) {
            orderDetails.setPromotions(
                    itemVariant.getAppliedPromotions()
                            .stream()
                            .map(AppliedPromotion::promotion)
                            .collect(Collectors.toUnmodifiableSet())
            );
        }

        return orderDetails;
    }

    private static Set<OrderDetails> toOrderDetails(Order order, List<ItemVariant> itemVariants) {
        Set<OrderDetails> details = new LinkedHashSet<>();

        for (ItemVariant itemVariant : itemVariants) {
            details.add(toOrderDetail(order, itemVariant));
        }

        return details;
    }

    private static PricingDetails toPricingDetails(DeliveryOption deliveryOption, Set<OrderDetails> orderDetails) {
        BigDecimal subtotal = sum(orderDetails, OrderDetails::getSubtotal);
        BigDecimal discountAmount = sum(orderDetails, OrderDetails::getTotalDiscountAmount);
        BigDecimal total = sum(orderDetails, OrderDetails::getTotal);
        BigDecimal deliveryCost = deliveryOption.calculateTotal();

        return new PricingDetails(
                CurrencyCode.IRR, //TODO: derive from item variant currency, not hardcoded
                subtotal,
                deliveryCost,
                BigDecimal.ZERO,
                BigDecimal.ZERO, //TODO: additional charges needed
                discountAmount,
                total.add(deliveryCost)
        );
    }

    // ==================================== HELPERS ====================================

    private static BigDecimal sum(Set<OrderDetails> orderDetails, Function<OrderDetails, BigDecimal> extractor) {
        return orderDetails.stream()
                .map(extractor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}