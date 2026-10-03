package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.provider.zarinpal.ZarinpalGateway;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Tag("integration")
//@EnabledIfEnvironmentVariable(named = "ZARINPAL_SANDBOX_MERCHANT_ID", matches = ".+")
class ZarinpalGatewayTest {

    @Autowired
    private ZarinpalGateway gateway;

    @Test
    void shouldGetAuthorityFromSandbox() {
        var result = gateway.initiate(
                new PaymentInitiateRequest(
                        PaymentGatewayType.ZARINPAL,
                        1L,
                        10_000L,
                        "sandbox test",
                        "http://localhost:8080/payments/callback",
                        "09121234567",
                        null
                )
        );

        assertThat(result.authority()).isNotBlank();
        assertThat(result.redirectUrl()).contains(result.authority());
    }
}