package com.sashia.ecommerce.catalog.serviceoffering;

import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingCreateRequest;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceOfferingResponse;
import com.sashia.ecommerce.catalog.serviceoffering.dto.ServiceUpdateDTO;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.Language;
import com.sashia.shared.TestWithLocale;
import com.sashia.shared.exception.InvalidResourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Service Controller Tests")
class ServiceOfferingControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/services";
    private static final long EXISTING_SERVICE_ID = 1L;
    private static final long NON_EXISTENT_SERVICE_ID = 0L;
    private static final long INVALID_SERVICE_GROUP_ID = 0L;
    private static final long VALID_SERVICE_GROUP_ID = 1L;
    private static final long NEW_SERVICE_ID = 4L;

    private static final String SERVICE_NAME = "test";
    private static final String UPDATED_SERVICE_NAME = "updated-test";
    private static final BigDecimal SERVICE_COST = new BigDecimal("100000");
    private static final BigDecimal UPDATED_SERVICE_COST = new BigDecimal("150000");
    private static final String SERVICE_DESCRIPTION = "Test description";
    private static final String SPECIAL_CHARS_NAME = "test-!@#$%";

    @Autowired
    private ServiceOfferingService serviceOfferingService;

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllServices {

        @Test
        @WithMockUser(authorities = "READ_ALL_SERVICES")
        @DisplayName("Should return all services with status 200")
        void shouldReturnAllServices() throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(3))
                    .andExpect(jsonPath("$.page.totalElements").value(3))
                    .andExpect(jsonPath("$.page.totalPages").value(1));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_SERVICES")
        @DisplayName("Should support pagination parameters")
        void shouldSupportPagination() throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL)
                    .param("page", "0")
                    .param("size", "5")
                    .param("sort", "name,asc")
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.page.number").value(0))
                    .andExpect(jsonPath("$.page.size").value(5))
                    .andExpect(jsonPath("$.content.length()").value(3));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_SERVICES")
        @DisplayName("Should filter services by name")
        void shouldFilterServicesByName() throws Exception {
            // Given
            String searchTerm = "اسنپ";

            // When
            ResultActions result = mockMvc().perform(get(BASE_URL)
                    .param("name", searchTerm)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].name").value(searchTerm));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_SERVICES")
        @DisplayName("Should return empty page when no services match criteria")
        void shouldReturnEmptyPage_whenNoServicesMatch() throws Exception {
            // Given
            String searchTerm = "non-existent-service";

            // When
            ResultActions result = mockMvc().perform(get(BASE_URL)
                    .param("name", searchTerm)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.page.totalElements").value(0));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("GET " + BASE_URL + "/{id}")
    class GetServiceOfferingById {

        @Test
        @WithMockUser(authorities = "READ_SERVICE")
        @DisplayName("Should return service when ID exists")
        void shouldReturnService_whenIdExists() throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_SERVICE_ID))
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.cost").isNumber())
                    .andExpect(jsonPath("$.serviceGroupId").isNumber());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isForbidden());
        }

        @TestWithLocale
        @WithMockUser(authorities = "READ_SERVICE")
        @DisplayName("Should return 422 when service ID doesn't exist")
        void shouldReturnUnprocessableContent_whenIdDoesNotExist(Language language) throws Exception {
            // When
            ResultActions result = mockMvc().perform(get(BASE_URL + "/{id}", NON_EXISTENT_SERVICE_ID)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isUnprocessableContent())
                    .andExpect(jsonPath("$.message").value(message("service.not.found", language.getLocale())));
        }

    }

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateServiceOffering {

        @Test
        @DirtiesContext
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should create service and return 201 with location header")
        void shouldCreateService() throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    SERVICE_NAME, SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isCreated())
                    .andExpect(header().string("Location", BASE_URL + "/" + NEW_SERVICE_ID));

            // Verify in database
            ServiceOfferingResponse createdService = assertDoesNotThrow(() -> serviceOfferingService.get(NEW_SERVICE_ID));

//            assertAll("Verify created service properties",
//                    () -> assertThat(createdService.name()).isEqualTo(request.name()),
//                    () -> assertThat(createdService.cost()).isEqualTo(request.cost()),
//                    () -> assertThat(createdService.serviceGroupId()).isEqualTo(request.serviceGroupId()),
//                    () -> assertThat(createdService.description()).isEqualTo(request.description())
//            );
        }

        @Test
        @DirtiesContext
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should create service with special characters in name")
        void shouldCreateServiceWithSpecialCharacters() throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    SPECIAL_CHARS_NAME, SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isCreated());
        }

        @Test
        @DirtiesContext
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should create service without description")
        void shouldCreateServiceWithoutDescription() throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    SERVICE_NAME, SERVICE_COST, VALID_SERVICE_GROUP_ID, null
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isCreated());

            ServiceOfferingResponse createdService = assertDoesNotThrow(() -> serviceOfferingService.get(NEW_SERVICE_ID));
//            assertThat(createdService.description()).isNull();
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest_whenRequiredFieldsMissing(Language language) throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(null, null, null, null);

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("service.name.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.cost").value(message("service.cost.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.serviceGroupId").value(message("service.serviceGroup.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should return 400 when name is empty")
        void shouldReturnBadRequest_whenNameIsEmpty(Language language) throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    "", SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("service.name.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_SERVICE")
        @DisplayName("Should return 422 when service group ID doesn't exist")
        void shouldReturnUnprocessableContent_whenServiceGroupNotFound(Language language) throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    SERVICE_NAME, SERVICE_COST, INVALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isUnprocessableContent())
                    .andExpect(jsonPath("$.message").value(message("service.serviceGroup.invalid", language.getLocale())));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            // Given
            ServiceOfferingCreateRequest request = new ServiceOfferingCreateRequest(
                    SERVICE_NAME, SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("PUT " + BASE_URL + "/{id}")
    class UpdateServiceOffering {

        @Test
        @DirtiesContext
        @WithMockUser(authorities = "UPDATE_SERVICE")
        @DisplayName("Should update service and return 204")
        void shouldUpdateService() throws Exception {
            // Given
            ServiceUpdateDTO request = new ServiceUpdateDTO(
                    UPDATED_SERVICE_NAME, UPDATED_SERVICE_COST, VALID_SERVICE_GROUP_ID, "Updated description"
            );

            // When
            ResultActions result = mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isNoContent());

            // Verify in database
            ServiceOfferingResponse updatedService = assertDoesNotThrow(() -> serviceOfferingService.get(EXISTING_SERVICE_ID));

//            assertAll("Verify updated service properties",
//                    () -> assertThat(updatedService.name()).isEqualTo(request.name()),
//                    () -> assertThat(updatedService.cost()).isEqualTo(request.cost()),
//                    () -> assertThat(updatedService.serviceGroupId()).isEqualTo(request.serviceGroupId()),
//                    () -> assertThat(updatedService.description()).isEqualTo(request.description())
//            );
        }

        @Test
        @WithMockUser(authorities = "UPDATE_SERVICE")
        @DisplayName("Should return 404 when service ID doesn't exist")
        void shouldReturnUnprocessableContent_whenServiceDoesNotExist() throws Exception {
            // Given
            ServiceUpdateDTO request = new ServiceUpdateDTO(
                    UPDATED_SERVICE_NAME, UPDATED_SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(put(BASE_URL + "/{id}", NON_EXISTENT_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isUnprocessableContent());
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_SERVICE")
        @DisplayName("Should return 404 when service group ID doesn't exist")
        void shouldReturnUnprocessableEntity_whenServiceGroupNotFound(Language language) throws Exception {
            // Given
            ServiceUpdateDTO request = new ServiceUpdateDTO(
                    SERVICE_NAME, SERVICE_COST, INVALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.message").value(message("service.serviceGroup.invalid", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_SERVICE")
        @DisplayName("Should return 400 when cost is negative")
        void shouldReturnBadRequest_whenCostIsNegative(Language language) throws Exception {
            // Given
            ServiceUpdateDTO request = new ServiceUpdateDTO(
                    SERVICE_NAME, new BigDecimal("-100"), VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .locale(language.getLocale())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isBadRequest()) //TODO: messages for min & max needed
                    .andDo(print());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            // Given
            ServiceUpdateDTO request = new ServiceUpdateDTO(
                    UPDATED_SERVICE_NAME, UPDATED_SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );

            // When
            ResultActions result = mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper().writeValueAsString(request)));

            // Then
            result.andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("DELETE " + BASE_URL + "/{id}")
    class DeleteServiceOffering {

        @Test
        @DirtiesContext
        @WithMockUser(authorities = "DELETE_SERVICE")
        @DisplayName("Should delete service and return 204")
        void shouldDeleteService() throws Exception {
            // Given - Create a service to delete
            ServiceOfferingCreateRequest createRequest = new ServiceOfferingCreateRequest(
                    "service-to-delete", SERVICE_COST, VALID_SERVICE_GROUP_ID, SERVICE_DESCRIPTION
            );
            Long serviceId = serviceOfferingService.create(createRequest);

            // When
            ResultActions result = mockMvc().perform(delete(BASE_URL + "/{id}", serviceId)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isNoContent());

            // Verify deletion
            assertThrows(InvalidResourceException.class, () -> serviceOfferingService.get(serviceId));
        }

        @Test
        @WithMockUser(authorities = "DELETE_SERVICE")
        @DisplayName("Should return 422 when service ID doesn't exist")
        void shouldReturnUnprocessableContent_whenServiceDoesNotExist() throws Exception {
            // When
            ResultActions result = mockMvc().perform(delete(BASE_URL + "/{id}", NON_EXISTENT_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            // When
            ResultActions result = mockMvc().perform(delete(BASE_URL + "/{id}", EXISTING_SERVICE_ID)
                    .contentType(MediaType.APPLICATION_JSON));

            // Then
            result.andExpect(status().isForbidden());
        }

    }

}