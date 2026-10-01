package com.sashia.ecommerce.billing.payment.opg;

import com.sashia.ecommerce.billing.payment.opg.dto.PaymentInitiateRequest;
import com.sashia.ecommerce.billing.payment.opg.provider.zarinpal.ZarinpalGateway;
import com.sashia.shared.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "ZARINPAL.MERCHANT_ID", matches = ".+")
class ZarinpalGatewayTest extends BaseControllerTest {

    @Autowired
    ZarinpalGateway zarinpalGateway;

    @Test
    void shouldGetAuthorityFromSandbox() {
        var result = zarinpalGateway.initiate(
                new PaymentInitiateRequest(
                        PaymentGatewayType.ZARINPAL,
                        1L,
                        10_000L,
                        "sandbox test",
                        null,
                        "09121234567",
                        null
                ));

        assertThat(result.authority()).isNotBlank();
        assertThat(result.redirectUrl()).contains(result.authority());
    }
}
