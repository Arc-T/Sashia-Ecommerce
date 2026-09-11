package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethodDTO;
import com.sashia.ecommerce.catalog.item.ItemVariantDTO;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryDTO;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.ItemDeliveryDto;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.WithSashiaUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/orders";

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateOrder {

        @Test
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should create order and return 201 with location header")
        void shouldCreateOrderWithoutPromotion() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(getRequest())))
                    .andExpect(status().isCreated());
        }

        //TODO: Get all the dtos from database not hardcoded !
        private static CheckoutRequest getRequest() {
            var paymentMethod = new PaymentMethodDTO(1L, null, null, null, null);

            var itemsList = List.of(
                    new ItemSummaryDTO(1L, null, null, List.of(new ItemVariantDTO(2L, 1, null))),
                    new ItemSummaryDTO(2L, null, null, List.of(new ItemVariantDTO(3L, 1, null))),
                    new ItemSummaryDTO(3L, null, null, List.of(new ItemVariantDTO(4L, 1, null)))
            );
//            80000.00 + 65000.00 + 21000.00(4200.00)
            var itemDelivery = new ItemDeliveryDto(1L, "test address", "taha",
                    "09361629708", "hajivandtaha@gmail.com");

            return new CheckoutRequest(null, null, itemDelivery, itemsList, paymentMethod, BigDecimal.valueOf(80000.00 + 65000.00 + 21000.00));
        }
    }

}
