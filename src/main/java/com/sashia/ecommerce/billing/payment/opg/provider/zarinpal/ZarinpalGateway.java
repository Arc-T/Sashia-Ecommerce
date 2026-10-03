package com.sashia.ecommerce.billing.payment.opg.provider.zarinpal;

import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.billing.payment.opg.AbstractPaymentGateway;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateResult;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyRequest;
import com.sashia.ecommerce.billing.payment.opg.dto.PaymentVerifyResult;
import com.sashia.shared.exception.BusinessRuleException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "payment.gateways.zarinpal")
public class ZarinpalGateway extends AbstractPaymentGateway {

    private String merchantId;
    private String requestUrl;
    private String verifyUrl;
    private String startPayUrl;
    private String callbackUrl;
    private String currency;
    private boolean sandbox;

    private final RestClient restClient;

    public ZarinpalGateway(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    @Override
    public PaymentGatewayType type() {
        return PaymentGatewayType.ZARINPAL;
    }

    @Override
    protected PaymentInitiateResult doInitiate(PaymentInitiateRequest request) {
        ZarinpalRequest body = toRequestBody(request);

        ZarinpalResponse response = restClient.post()
                .uri(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(ZarinpalResponse.class);

        if (response == null || !response.isSuccess()) {
            logger().warn("Zarinpal initiate rejected | response={}", response);
            throw new BusinessRuleException("payment.gateway.initiate.failed");
        }

        ZarinpalResponse.Data data = response.data();
        String redirectUrl = startPayUrl + data.authority();

        return new PaymentInitiateResult(
                type(),
                data.authority(),
                redirectUrl,
                data.feeType(),
                data.fee()
        );
    }

    @Override
    protected PaymentVerifyResult doVerify(PaymentVerifyRequest request) {
        Map<String, Object> body = Map.of(
                "merchant_id", merchantId,
                "amount", request.amount().intValue(),
                "authority", request.authority()
        );

        ZarinpalResponse response = restClient.post()
                .uri(verifyUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(ZarinpalResponse.class);

        if (response == null || response.data() == null) {
            throw new BusinessRuleException("payment.gateway.verify.failed");
        }

        ZarinpalResponse.Data data = response.data();
        boolean paid = data.code() != null && (data.code() == 100 || data.code() == 101);

        return new PaymentVerifyResult(
                type(),
                paid ? PaymentStatus.PAID : PaymentStatus.FAILED,
                request.authority(),
                data.refId() != null ? String.valueOf(data.refId()) : null,
                data.feeType(),
                data.fee()
        );
    }

    private ZarinpalRequest toRequestBody(PaymentInitiateRequest request) {
        Map<String, String> metadata = new HashMap<>();
        if (request.mobile() != null) {
            metadata.put("mobile", request.mobile());
        }
        if (request.email() != null) {
            metadata.put("email", request.email());
        }
        metadata.put("order_id", String.valueOf(request.orderId()));

        String callback = request.callbackUrl() != null && !request.callbackUrl().isBlank()
                ? request.callbackUrl()
                : callbackUrl;

        return new ZarinpalRequest(
                merchantId,
                request.amount().intValue(),
                currency,
                request.description() != null ? request.description() : "Order " + request.orderId(),
                callback,
                null,
                metadata
        );
    }
}