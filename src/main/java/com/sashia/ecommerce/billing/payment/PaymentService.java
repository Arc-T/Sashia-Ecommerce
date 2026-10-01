package com.sashia.ecommerce.billing.payment;

import com.sashia.ecommerce.billing.payment.dto.PaymentDTO;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;

import java.util.Optional;

/**
 * Application service for payments: gateway orchestration + persistence.
 */
public interface PaymentService {

    /**
     * Initiate payment at the selected gateway and persist a PENDING payment row.
     */
    PaymentInitiateResult initiate(PaymentInitiateRequest request);

    /**
     * Verify payment at the gateway (callback / client return) and update the payment row.
     */
    PaymentVerifyResult verify(PaymentVerifyRequest request);

    Optional<PaymentDTO> get(Long id);

    Optional<PaymentDTO> getByAuthority(String authority);

    PaymentVerifyResult verifyCallback(String authority, String status, PaymentGatewayType gatewayType);
}
