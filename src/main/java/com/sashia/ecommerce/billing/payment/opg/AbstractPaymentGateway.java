package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;
import com.sashia.shared.exception.BusinessRuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template Method base for payment gateways.
 * <p>
 * Handles cross-cutting concerns that every provider needs:
 * <ul>
 *   <li>structured logging (start / success / failure)</li>
 *   <li>basic request validation</li>
 *   <li>elapsed-time measurement</li>
 * </ul>
 * Concrete gateways only implement {@link #doInitiate} and {@link #doVerify}.
 */
public abstract class AbstractPaymentGateway implements PaymentGateway {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public final PaymentInitiateResult initiate(PaymentInitiateRequest request) {
        long started = System.currentTimeMillis();
        log.info("[{}] initiate start | orderId={} amount={}", type(), request.orderId(), request.amount());

        try {
            PaymentInitiateResult result = doInitiate(request);
            log.info("[{}] initiate success | orderId={} authority={} durationMs={}",
                    type(), request.orderId(), result.authority(), elapsed(started));
            return result;
        } catch (Exception e) {
            log.error("[{}] initiate failed | orderId={} durationMs={} reason={}",
                    type(), request.orderId(), elapsed(started), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public final PaymentVerifyResult verify(PaymentVerifyRequest request) {
        long started = System.currentTimeMillis();
        log.info("[{}] verify start | orderId={} authority={}",
                type(), request.orderId(), request.authority());

        try {
            PaymentVerifyResult result = doVerify(request);
            log.info("[{}] verify success | orderId={} status={} ref={} durationMs={}",
                    type(), request.orderId(), result.status(), result.referenceId(), elapsed(started));
            return result;
        } catch (Exception e) {
            log.error("[{}] verify failed | orderId={} authority={} durationMs={} reason={}",
                    type(), request.orderId(), request.authority(), elapsed(started), e.getMessage(), e);
            throw e;
        }
    }

    // -------------------------------------------------------------------------
    // Hooks for subclasses
    // -------------------------------------------------------------------------

    /**
     * Provider-specific payment initiation (HTTP call, map response, etc.).
     */
    protected abstract PaymentInitiateResult doInitiate(PaymentInitiateRequest request);

    /**
     * Provider-specific payment verification.
     */
    protected abstract PaymentVerifyResult doVerify(PaymentVerifyRequest request);

    // -------------------------------------------------------------------------
    // Helpers available to subclasses
    // -------------------------------------------------------------------------

    protected Logger logger() {
        return log;
    }

    private static long elapsed(long startedMs) {
        return System.currentTimeMillis() - startedMs;
    }
}
