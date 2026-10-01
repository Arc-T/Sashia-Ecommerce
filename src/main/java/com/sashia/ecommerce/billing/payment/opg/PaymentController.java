package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.PaymentService;
import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/payments")
class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @PreAuthorize("hasAuthority('CREATE_PAYMENT') or isAuthenticated()")
    ResponseEntity<PaymentInitiateResult> initiate(@RequestBody @Valid PaymentInitiateRequest request) {
        return ResponseEntity.ok(paymentService.initiate(request));
    }

    @PostMapping("/verify")
    @PreAuthorize("hasAuthority('VERIFY_PAYMENT') or isAuthenticated()")
    ResponseEntity<PaymentVerifyResult> verify(@RequestBody @Valid PaymentVerifyRequest request) {
        return ResponseEntity.ok(paymentService.verify(request));
    }

    @GetMapping("/callback")
    ResponseEntity<PaymentVerifyResult> callback(
            @RequestParam("Authority") String authority,
            @RequestParam(value = "Status", required = false) String status,
            @RequestParam(value = "gateway", defaultValue = "ZARINPAL") PaymentGatewayType gatewayType) {
        return ResponseEntity.ok(paymentService.verifyCallback(authority, status, gatewayType));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PAYMENT') or isAuthenticated()")
    ResponseEntity<PaymentDTO> get(@PathVariable Long id) {
        return ResponseEntity.of(paymentService.get(id));
    }
}