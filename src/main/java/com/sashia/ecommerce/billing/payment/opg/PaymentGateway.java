package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;

/**
 * Strategy contract for an online payment gateway provider.
 * <p>
 * Each provider (Zarinpal, IDPay, NextPay, …) implements this interface.
 * Prefer extending {@link AbstractPaymentGateway} so logging, timing and
 * common error handling stay in one place.
 */
public interface PaymentGateway {

    /**
     * Unique type this implementation handles.
     */
    PaymentGatewayType type();

    /**
     * Start a payment session at the provider and return redirect info.
     */
    PaymentInitiateResult initiate(PaymentInitiateRequest request);

    /**
     * Verify a payment after the user returns from the provider.
     */
    PaymentVerifyResult verify(PaymentVerifyRequest request);
}
