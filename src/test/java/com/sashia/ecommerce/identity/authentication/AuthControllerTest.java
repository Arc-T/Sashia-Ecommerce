package com.sashia.ecommerce.identity.authentication;

import com.sashia.ecommerce.identity.authentication.dto.LoginRequest;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.Language;
import com.sashia.shared.TestWithLocale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Authentication Controller Tests")
class AuthControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/authentication";

    @Nested
    @DisplayName("POST " + BASE_URL)
    class Login {

        @Test
        @DisplayName("Should authenticate user and return JWT")
        void shouldAuthenticateUser() throws Exception {
            LoginRequest request = new LoginRequest(
                    "361629708",
                    "1234321",
                    false
            );

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").exists())
                    .andDo(print());
        }

        @TestWithLocale
        @DisplayName("Should return 401 when credentials are invalid")
        void shouldReturnUnauthorized_whenCredentialsAreInvalid(Language language) throws Exception {
            LoginRequest request = new LoginRequest(
                    "00000000000",
                    "88888888888",
                    false
            );

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message")
                            .value(message(
                                    "authentication.invalid",
                                    language.getLocale()
                            )));
        }

        @TestWithLocale
        @DisplayName("Should return 400 when login request validation fails")
        void shouldReturnBadRequest_whenRequestIsInvalid(Language language) throws Exception {
            LoginRequest request = new LoginRequest(
                    null,
                    null,
                    false
            );

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.username")
                            .value(message("username.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.password")
                            .value(message("password.required", language.getLocale())));
        }

    }

    @Nested
    @DisplayName("POST " + BASE_URL + "/validate")
    class ValidateToken {

        @Test
        @DisplayName("Should validate a valid JWT")
        void shouldValidateToken() throws Exception {
            String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzNjE2Mjk3MDgiLCJhdWQiOiJzYXNoaWEtZWNvbW1lcmNlIiwic2NwIjpbIlVQREFURV9DQVRFR09SWSIsIlVQREFURV9UQUciLCJERUxFVEVfU0VSVklDRSIsIkRFTEVURV9PUkRFUiIsIkRFTEVURV9BVFRSSUJVVEUiLCJDUkVBVEVfVEFHIiwiREVMRVRFX0RJU0NPVU5UIiwiUkVBRF9BTExfRElTQ09VTlRTIiwiUkVBRF9TRVJWSUNFX0dST1VQIiwiUkVBRF9BTExfU0VSVklDRV9HUk9VUFMiLCJSRUFEX0RJU0NPVU5UIiwiUkVBRF9BTExfQVRUUklCVVRFX1RZUEVTIiwiQ1JFQVRFX0NBVEVHT1JZIiwiQ1JFQVRFX0FUVFJJQlVURSIsIlVQREFURV9QUk9EVUNUIiwiQ1JFQVRFX1NFUlZJQ0UiLCJSRUFEX0FMTF9UQUdTIiwiVVBEQVRFX1NFUlZJQ0UiLCJERUxFVEVfUFJPRFVDVCIsIkRFTEVURV9DQVRFR09SWSIsIlJPTEVfU1VQRVJfQURNSU4iLCJSRUFEX0FMTF9BVFRSSUJVVEVTIiwiVVBEQVRFX0RJU0NPVU5UIiwiUkVBRF9BTExfU0VSVklDRVMiLCJDUkVBVEVfU0VSVklDRV9HUk9VUCIsIlJFQURfQVRUUklCVVRFIiwiUkVBRF9BTExfQ0FURUdPUklFUyIsIlVQREFURV9TRVJWSUNFX0dST1VQIiwiUkVBRF9PUkRFUiIsIlJPTEVfU0hPUF9PV05FUiIsIkRFTEVURV9UQUciLCJVUERBVEVfT1JERVIiLCJSRUFEX1NFUlZJQ0UiLCJSRUFEX1RBRyIsIkRFTEVURV9TRVJWSUNFX0dST1VQIiwiVVBEQVRFX0FUVFJJQlVURSIsIkNSRUFURV9PUkRFUiIsIkNSRUFURV9QUk9EVUNUIiwiUkVBRF9DQVRFR09SWSIsIlJFQURfQUxMX1BST0RVQ1RTIiwiUkVBRF9BTExfT1JERVJTIiwiUkVBRF9QUk9EVUNUIiwiQ1JFQVRFX0RJU0NPVU5UIiwiRkFDVE9SX1BBU1NXT1JEIl0sImlzcyI6Imh0dHBzOi8vc2FzaGlhLmRldiIsImV4cCI6MTc4NzI4NTM1MSwiaWF0IjoxNzgzNjg1MzUxLCJqdGkiOiJlMDdmNjQ5Zi1jY2E2LTQ2M2MtYjA3Yi05NGEzYjE4YTA0MzkifQ.D_-CBn9n-oIjAwBsVLROp9QzqzV6if7UPXxrMiPEmDYhhSj1V5WYUDnJuWxvpD6C1IpCG2GkHHFHgVM__GrUcA";

            mockMvc().perform(post(BASE_URL + "/validate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(
                                    "Authorization", "Bearer " + token
                            ))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 401 when JWT is invalid")
        void shouldReturnUnauthorized_whenTokenIsInvalid() throws Exception {
            mockMvc().perform(post(BASE_URL + "/validate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(
                                    "Authorization",
                                    "Bearer invalid-token"
                            ))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.message")
                            .value("authentication.invalid"));
        }

    }

}