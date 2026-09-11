package com.sashia.ecommerce.billing.payment.opg;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/payments")
public class PaymentController {

    private final PaymentLockService paymentLockService;

    public PaymentController(PaymentLockService paymentLockService) {
        this.paymentLockService = paymentLockService;
    }

//    @PostMapping
//    public ResponseEntity<?> initiatePayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
//        AtomicReference<PaymentGatewayInfoDto> gatewayInfoRef = new AtomicReference<>();
//
//        paymentLockService.executeTask(paymentRequestDto, () -> {
//            gatewayInfoRef.set(gatewayInfoDto);
//        });
//
//        return ResponseEntity.ok(gatewayInfoRef.get());
//    }

}
