package com.sashia.ecommerce.ordering.order.internal;

import com.sashia.ecommerce.ordering.order.OrderService;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.OrderDTO;
import com.sashia.ecommerce.ordering.order.dto.OrderSearchDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/orders")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_ORDERS')")
    ResponseEntity<Page<OrderDTO>> readAll(Pageable pageable, OrderSearchDTO search) {
        return ResponseEntity.ok(orderService.getAll(pageable, search));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('READ_MY_ORDERS')")
    ResponseEntity<Page<OrderDTO>> readAllMine(Pageable pageable, OrderSearchDTO search) {
        return ResponseEntity.ok(orderService.getAll(pageable, search));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ORDER')")
    ResponseEntity<Long> create(@RequestBody @Valid CheckoutRequest request) {
        return ResponseEntity.ok(orderService.create(request));
    }

}