package com.sashia.ecommerce.ordering.order.internal;

import com.sashia.ecommerce.ordering.order.Order;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;

public class OrderMapper {

    public static Order toEntity(CheckoutRequest request) {
        return null;
    }


//    public static OrderDTO toDTO(Order order) {
//        return new OrderDTO(
//                order.getId(),
//                order.getUserId(),
//                BigDecimal.ONE,
//                order.getDescription(),
//                order.getCreatedAt(),
//                order.getUpdatedAt()
//        );
//    }

}