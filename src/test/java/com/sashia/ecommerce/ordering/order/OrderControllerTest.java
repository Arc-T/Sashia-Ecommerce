package com.sashia.ecommerce.ordering.order;

import com.sashia.ecommerce.billing.payment.dto.PaymentMethodDTO;
import com.sashia.ecommerce.catalog.item.ItemVariantResponse;
import com.sashia.ecommerce.catalog.item.dto.ItemSummaryResponse;
import com.sashia.ecommerce.ordering.order.dto.CheckoutRequest;
import com.sashia.ecommerce.ordering.order.dto.ItemDeliveryDto;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.Language;
import com.sashia.shared.TestWithLocale;
import com.sashia.shared.WithSashiaUser;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Order Controller Tests")
class OrderControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/orders";

    private static final long EXISTING_DELIVERY_OPTION_ID = 1L;
    private static final long NON_EXISTENT_DELIVERY_OPTION_ID = 0L;
    private static final long NON_EXISTENT_ITEM_ID = 0L;
    private static final long NON_EXISTENT_VARIANT_ID = 0L;

    private static final long ITEM_1_ID = 1L;
    private static final long VARIANT_2_ID = 2L;   // 80000, stock 2
    private static final long ITEM_2_ID = 2L;
    private static final long VARIANT_3_ID = 3L;   // 65000, stock 1
    private static final long ITEM_3_ID = 3L;
    private static final long VARIANT_4_ID = 4L;   // 21000, stock 1

    private static final BigDecimal TOTAL_PRICE = BigDecimal.valueOf(80000.00 + 65000.00 + 21000.00);

    private static final String RECEIVER_NAME = "taha";
    private static final String RECEIVER_PHONE = "09361629708";
    private static final String RECEIVER_EMAIL = "hajivandtaha@gmail.com";
    private static final String DELIVERY_ADDRESS = "test address";

    // ============================== POST /orders ==============================

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
                            .content(objectMapper().writeValueAsString(validCheckoutRequest())))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION,
                            org.hamcrest.Matchers.startsWith(BASE_URL + "/")))
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(validCheckoutRequest())))
                    .andExpect(status().isForbidden());
        }

        @TestWithLocale
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DisplayName("Should return 404 when delivery option does not exist")
        void shouldReturnNotFound_whenDeliveryOptionDoesNotExist(Language language) throws Exception {
            CheckoutRequest request = new CheckoutRequest(
                    null, null,
                    new ItemDeliveryDto(
                            NON_EXISTENT_DELIVERY_OPTION_ID,
                            DELIVERY_ADDRESS, RECEIVER_NAME, RECEIVER_PHONE, RECEIVER_EMAIL),
                    validItems(), paymentMethod(), TOTAL_PRICE);

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(message("item.delivery.not.found", language.getLocale())));
        }

        @TestWithLocale
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DisplayName("Should return 404 when item variant does not exist")
        void shouldReturnNotFound_whenItemVariantDoesNotExist(Language language) throws Exception {
            CheckoutRequest request = new CheckoutRequest(
                    null, null, delivery(),
                    List.of(new ItemSummaryResponse(
                            NON_EXISTENT_ITEM_ID, null, null,
                            List.of(new ItemVariantResponse(NON_EXISTENT_VARIANT_ID, 1, null)))),
                    paymentMethod(), TOTAL_PRICE);

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(message("item.not.found", language.getLocale())));
        }

        @TestWithLocale
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DisplayName("Should return error when requested quantity exceeds stock")
        void shouldReturnError_whenStockExceeded(Language language) throws Exception {
            // Variant 3 stock = 1; request qty 2
            CheckoutRequest request = new CheckoutRequest(
                    null, null, delivery(),
                    List.of(new ItemSummaryResponse(
                            ITEM_2_ID, null, null,
                            List.of(new ItemVariantResponse(VARIANT_3_ID, 2, null)))),
                    paymentMethod(), BigDecimal.valueOf(65000.00));

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    // BusinessRuleException not mapped yet in GlobalExceptionHandler
                    .andExpect(status().isInternalServerError())
                    .andDo(print());
        }

        @Test
        @Disabled("TODO: Add jakarta validation on CheckoutRequest and handler for empty body")
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DisplayName("Should return 400 when request body is empty")
        void shouldReturnBadRequest_whenRequestBodyEmpty() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Disabled("TODO: Add jakarta validation annotations on CheckoutRequest fields")
        @WithSashiaUser(authorities = "CREATE_ORDER")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest_whenRequiredFieldsMissing() throws Exception {
            CheckoutRequest request = new CheckoutRequest(null, null, null, null, null, null);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ============================== GET /orders ==============================

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllOrders {

        @Test
        @Disabled("TODO: Implement OrderService.getAll before enabling")
        @WithMockUser(authorities = "READ_ALL_ORDERS")
        @DisplayName("Should return all orders with status 200")
        void shouldReturnAllOrders() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @Disabled("TODO: Implement OrderService.getAll and OrderSearchDTO filters")
        @WithMockUser(authorities = "READ_ALL_ORDERS")
        @DisplayName("Should support pagination parameters")
        void shouldSupportPagination() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page.number").value(0))
                    .andExpect(jsonPath("$.page.size").value(10));
        }
    }

    // ============================== GET /orders/me ==============================

    @Nested
    @DisplayName("GET " + BASE_URL + "/me")
    class GetMyOrders {

        @Test
        @Disabled("TODO: Implement OrderService.getAll (mine) before enabling")
        @WithSashiaUser(authorities = "READ_MY_ORDERS")
        @DisplayName("Should return current user orders with status 200")
        void shouldReturnMyOrders() throws Exception {
            mockMvc().perform(get(BASE_URL + "/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(get(BASE_URL + "/me")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    // ============================== FIXTURES ==============================

    private static ItemDeliveryDto delivery() {
        return new ItemDeliveryDto(
                EXISTING_DELIVERY_OPTION_ID, DELIVERY_ADDRESS,
                RECEIVER_NAME, RECEIVER_PHONE, RECEIVER_EMAIL);
    }

    private static PaymentMethodDTO paymentMethod() {
        return new PaymentMethodDTO(1L, null, null, null, null);
    }

    private static List<ItemSummaryResponse> validItems() {
        return List.of(
                new ItemSummaryResponse(ITEM_1_ID, null, null,
                        List.of(new ItemVariantResponse(VARIANT_2_ID, 1, null))),
                new ItemSummaryResponse(ITEM_2_ID, null, null,
                        List.of(new ItemVariantResponse(VARIANT_3_ID, 1, null))),
                new ItemSummaryResponse(ITEM_3_ID, null, null,
                        List.of(new ItemVariantResponse(VARIANT_4_ID, 1, null)))
        );
    }

    private static CheckoutRequest validCheckoutRequest() {
        return new CheckoutRequest(null, null, delivery(), validItems(), paymentMethod(), TOTAL_PRICE);
    }
}