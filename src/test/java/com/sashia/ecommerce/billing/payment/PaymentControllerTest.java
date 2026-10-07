package com.sashia.ecommerce.billing.payment;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethodDTO;
import com.sashia.ecommerce.billing.payment.dto.PaymentStatus;
import com.sashia.ecommerce.billing.payment.opg.PaymentGateway;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayRegistry;
import com.sashia.ecommerce.billing.payment.opg.PaymentGatewayType;
import com.sashia.ecommerce.billing.payment.opg.dto.*;
import com.sashia.ecommerce.catalog.item.ItemVariantResponse;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.ordering.order.OrderService;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.ItemDeliveryDto;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.WithSashiaUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Payment Controller Tests")
class PaymentControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/payments";

    private static final String MOCK_AUTHORITY = "A00000000000000000000000000000000001";
    private static final String MOCK_REDIRECT = "https://sandbox.zarinpal.com/pg/StartPay/" + MOCK_AUTHORITY;
    private static final String MOCK_REF_ID = "123456789";
    private static final long AMOUNT = 166_000L;

    @MockitoBean
    private PaymentGatewayRegistry gatewayRegistry;

    @MockitoBean
    private PaymentGateway paymentGateway;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentRepository paymentRepository;

    @BeforeEach
    void stubGateway() {
        when(paymentGateway.type()).thenReturn(PaymentGatewayType.ZARINPAL);
        when(gatewayRegistry.get(eq(PaymentGatewayType.ZARINPAL))).thenReturn(paymentGateway);
        when(gatewayRegistry.get(any(PaymentGatewayType.class))).thenReturn(paymentGateway);
    }

    // ============================== POST /payments/initiate ==============================

    @Nested
    @DisplayName("POST " + BASE_URL + "/initiate")
    class InitiatePayment {

        @Test
        @WithSashiaUser(authorities = "CREATE_PAYMENT")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should initiate payment and return authority + redirectUrl")
        void shouldInitiatePayment() throws Exception {
            Long orderId = createPendingOrder();

            when(paymentGateway.initiate(any(PaymentInitiateRequest.class)))
                    .thenReturn(new PaymentInitiateResult(
                            PaymentGatewayType.ZARINPAL,
                            MOCK_AUTHORITY,
                            MOCK_REDIRECT,
                            "Merchant",
                            0
                    ));

            InitiatePaymentApiRequest body = new InitiatePaymentApiRequest(
                    PaymentGatewayType.ZARINPAL,
                    orderId,
                    AMOUNT,
                    "Order payment",
                    null,
                    "09361629708",
                    "hajivandtaha@gmail.com"
            );

            mockMvc().perform(post(BASE_URL + "/initiate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.gatewayType").value("ZARINPAL"))
                    .andExpect(jsonPath("$.authority").value(MOCK_AUTHORITY))
                    .andExpect(jsonPath("$.redirectUrl").value(MOCK_REDIRECT))
                    .andExpect(jsonPath("$.feeType").value("Merchant"))
                    .andDo(print());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_PAYMENT")
        @DisplayName("Should return 404 when order does not exist")
        void shouldReturnNotFound_whenOrderDoesNotExist() throws Exception {
            InitiatePaymentApiRequest body = new InitiatePaymentApiRequest(
                    PaymentGatewayType.ZARINPAL, 0L, AMOUNT, null, null, null, null);

            mockMvc().perform(post(BASE_URL + "/initiate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_PAYMENT")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest_whenRequiredFieldsMissing() throws Exception {
            InitiatePaymentApiRequest body = new InitiatePaymentApiRequest(
                    null, null, null, null, null, null, null);

            mockMvc().perform(post(BASE_URL + "/initiate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ============================== POST /payments/verify ==============================

    @Nested
    @DisplayName("POST " + BASE_URL + "/verify")
    class VerifyPayment {

        @Test
        @WithSashiaUser(authorities = {"CREATE_PAYMENT", "VERIFY_PAYMENT"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should verify payment, mark PAID, and transition order status")
        void shouldVerifyPaymentSuccessfully() throws Exception {
            Long orderId = createPendingOrder();
            initiateWithMockGateway(orderId);

            when(paymentGateway.verify(any(PaymentVerifyRequest.class)))
                    .thenReturn(new PaymentVerifyResult(
                            PaymentGatewayType.ZARINPAL,
                            PaymentStatus.PAID,
                            MOCK_AUTHORITY,
                            MOCK_REF_ID,
                            "Merchant",
                            0
                    ));

            VerifyPaymentApiRequest body = new VerifyPaymentApiRequest(
                    PaymentGatewayType.ZARINPAL,
                    MOCK_AUTHORITY,
                    AMOUNT,
                    orderId
            );

            mockMvc().perform(post(BASE_URL + "/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("PAID"))
                    .andExpect(jsonPath("$.authority").value(MOCK_AUTHORITY))
                    .andExpect(jsonPath("$.referenceId").value(MOCK_REF_ID))
                    .andDo(print());
        }

        @Test
        @WithSashiaUser(authorities = "VERIFY_PAYMENT")
        @DisplayName("Should return 404 when authority is unknown")
        void shouldReturnNotFound_whenAuthorityUnknown() throws Exception {
            VerifyPaymentApiRequest body = new VerifyPaymentApiRequest(
                    PaymentGatewayType.ZARINPAL,
                    "UNKNOWN_AUTHORITY",
                    AMOUNT,
                    1L
            );

            mockMvc().perform(post(BASE_URL + "/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_PAYMENT", "VERIFY_PAYMENT"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should return FAILED when gateway rejects payment")
        void shouldReturnFailed_whenGatewayRejects() throws Exception {
            Long orderId = createPendingOrder();
            initiateWithMockGateway(orderId);

            when(paymentGateway.verify(any(PaymentVerifyRequest.class)))
                    .thenReturn(new PaymentVerifyResult(
                            PaymentGatewayType.ZARINPAL,
                            PaymentStatus.FAILED,
                            MOCK_AUTHORITY,
                            null,
                            null,
                            null
                    ));

            VerifyPaymentApiRequest body = new VerifyPaymentApiRequest(
                    PaymentGatewayType.ZARINPAL,
                    MOCK_AUTHORITY,
                    AMOUNT,
                    orderId
            );

            mockMvc().perform(post(BASE_URL + "/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("FAILED"));
        }
    }

    // ============================== GET /payments/callback ==============================

    @Nested
    @DisplayName("GET " + BASE_URL + "/callback")
    class PaymentCallback {

        @Test
        @WithSashiaUser(authorities = "CREATE_PAYMENT")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should verify via callback when Status=OK")
        void shouldVerifyViaCallback_whenStatusOk() throws Exception {
            Long orderId = createPendingOrder();
            initiateWithMockGateway(orderId);

            when(paymentGateway.verify(any(PaymentVerifyRequest.class)))
                    .thenReturn(new PaymentVerifyResult(
                            PaymentGatewayType.ZARINPAL,
                            PaymentStatus.PAID,
                            MOCK_AUTHORITY,
                            MOCK_REF_ID,
                            null,
                            null
                    ));

            mockMvc().perform(get(BASE_URL + "/callback")
                            .param("Authority", MOCK_AUTHORITY)
                            .param("Status", "OK")
                            .param("gateway", "ZARINPAL"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("PAID"))
                    .andExpect(jsonPath("$.referenceId").value(MOCK_REF_ID))
                    .andDo(print());
        }

        @Test
        @DisplayName("Should return 404 when callback authority is unknown")
        void shouldReturnNotFound_whenCallbackAuthorityUnknown() throws Exception {
            mockMvc().perform(get(BASE_URL + "/callback")
                            .param("Authority", "DOES_NOT_EXIST")
                            .param("Status", "OK"))
                    .andExpect(status().isNotFound());
        }
    }

    // ============================== GET /payments/{id} ==============================

    @Nested
    @DisplayName("GET " + BASE_URL + "/{id}")
    class GetPayment {

        @Test
        @WithSashiaUser(authorities = {"CREATE_PAYMENT", "READ_PAYMENT"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should return payment when id exists")
        void shouldReturnPayment_whenIdExists() throws Exception {
            Long orderId = createPendingOrder();
            initiateWithMockGateway(orderId);

            Long paymentId = paymentRepository.findByAuthority(MOCK_AUTHORITY)
                    .orElseThrow()
                    .getId();

            mockMvc().perform(get(BASE_URL + "/{id}", paymentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(paymentId))
                    .andExpect(jsonPath("$.authority").value(MOCK_AUTHORITY))
                    .andExpect(jsonPath("$.status").value("PENDING"))
                    .andExpect(jsonPath("$.amount").value((int) AMOUNT))
                    .andDo(print());
        }

        @Test
        @WithSashiaUser(authorities = "READ_PAYMENT")
        @DisplayName("Should return 404 when payment id does not exist")
        void shouldReturnNotFound_whenIdDoesNotExist() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", 0L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    // ============================== FIXTURES ==============================

    private Long createPendingOrder() {
        return orderService.create(checkoutRequest());
    }

    private void initiateWithMockGateway(Long orderId) throws Exception {
        when(paymentGateway.initiate(any(PaymentInitiateRequest.class)))
                .thenReturn(new PaymentInitiateResult(
                        PaymentGatewayType.ZARINPAL,
                        MOCK_AUTHORITY,
                        MOCK_REDIRECT,
                        "Merchant",
                        0
                ));

        InitiatePaymentApiRequest body = new InitiatePaymentApiRequest(
                PaymentGatewayType.ZARINPAL,
                orderId,
                AMOUNT,
                "Order payment",
                null,
                null,
                null
        );

        mockMvc().perform(post(BASE_URL + "/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper().writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    private static CheckoutRequest checkoutRequest() {
        var paymentMethod = new PaymentMethodDTO(1L, null, null, null, null);
        var items = List.of(
                new ItemSummaryResponse(1L, null, null, List.of(new ItemVariantResponse(2L, 1, null))),
                new ItemSummaryResponse(2L, null, null, List.of(new ItemVariantResponse(3L, 1, null))),
                new ItemSummaryResponse(3L, null, null, List.of(new ItemVariantResponse(4L, 1, null)))
        );
        var delivery = new ItemDeliveryDto(1L, "test address", "taha",
                "09361629708", "hajivandtaha@gmail.com");
        return new CheckoutRequest(null, null, delivery, items, paymentMethod,
                BigDecimal.valueOf(AMOUNT));
    }
}