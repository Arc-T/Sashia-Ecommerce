package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Registry / factory for {@link PaymentGateway} strategies.
 * <p>
 * Spring injects every {@code PaymentGateway} bean; they are indexed by
 * {@link PaymentGatewayType}. Adding a new provider only requires a new
 * {@code @Component} that implements {@link PaymentGateway} — no change here.
 */
@Component
public class PaymentGatewayRegistry {

    private final Map<PaymentGatewayType, PaymentGateway> gateways;

    public PaymentGatewayRegistry(List<PaymentGateway> gatewayList) {
        this.gateways = new EnumMap<>(PaymentGatewayType.class);
        for (PaymentGateway gateway : gatewayList) {
            PaymentGateway previous = gateways.put(gateway.type(), gateway);
            if (previous != null) {
                throw new IllegalStateException(
                        "Duplicate PaymentGateway for type " + gateway.type()
                                + ": " + previous.getClass().getName()
                                + " and " + gateway.getClass().getName());
            }
        }
    }

    public PaymentGateway get(PaymentGatewayType type) {
        PaymentGateway gateway = gateways.get(type);
        if (gateway == null) {
            throw new ResourceNotFoundException("payment.gateway.not.found");
        }
        return gateway;
    }

    public boolean supports(PaymentGatewayType type) {
        return gateways.containsKey(type);
    }
}
