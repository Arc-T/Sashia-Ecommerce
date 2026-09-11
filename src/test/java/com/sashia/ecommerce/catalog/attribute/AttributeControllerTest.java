package com.sashia.ecommerce.catalog.attribute;

import com.sashia.ecommerce.catalog.attribute.dto.*;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.Language;
import com.sashia.shared.TestWithLocale;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Attribute Controller Tests")
class AttributeControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/attributes";
    private static final long EXISTING_ATTRIBUTE_ID = 1L;
    private static final long NON_EXISTENT_ATTRIBUTE_ID = 0L;
    private static final long INVALID_CATEGORY_ID = 0L;
    private static final long NEW_ATTRIBUTE_ID = 10L;

    private static final String ATTRIBUTE_NAME = "size";
    private static final long ATTRIBUTE_CATEGORY_ID = 1L;
    private static final AttributeType ATTRIBUTE_TYPE = AttributeType.SELECT;
    private static final boolean ATTRIBUTE_IS_FILTERABLE = true;
    private static final String ATTRIBUTE_DESCRIPTION = "lorem ipsum";
    private static final List<AttributeValueRequest> ATTRIBUTE_VALUES =
            List.of(new AttributeValueRequest("2X"), new AttributeValueRequest("3X"));

    @Autowired
    private AttributeService attributeService;

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllAttributes {

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTES")
        @DisplayName("Should return all attributes with status 200")
        void shouldReturnAllAttributes() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(9))
                    .andExpect(jsonPath("$.page.totalElements").value(9));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTES")
        @DisplayName("Should return one filtered attributes when search param matches")
        void shouldReturnFilteredAttributes_whenSearchParamMatchesOne() throws Exception {
            String searchTerm = "بند";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1));

        }

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTES")
        @DisplayName("Should return all filtered attributes when search param matches")
        void shouldReturnFilteredAttributes_whenSearchParamMatchesWithMoreThanOne() throws Exception {
            String searchTerm = "جنس";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTES")
        @DisplayName("Should return empty page when search param doesn't match")
        void shouldReturnEmptyPage_whenSearchParamDoesNotMatch() throws Exception {
            String searchTerm = "non-existent-term";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.page.totalElements").value(0));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andDo(print());
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTES")
        @DisplayName("Should support pagination parameters")
        void shouldSupportPagination() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "name,asc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page.number").value(0))
                    .andExpect(jsonPath("$.page.size").value(10));
        }

    }

    @Nested
    @DisplayName("GET " + BASE_URL + "/{id}")
    class GetAttributeById {

        @Test
        @WithMockUser(authorities = "READ_ATTRIBUTE")
        @DisplayName("Should return one filtered attributes when search param matches")
        void shouldReturnExistingAttribute() throws Exception {
            mockMvc().perform(get(BASE_URL + "/" + EXISTING_ATTRIBUTE_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateAttribute {

        @Test
        @WithMockUser(authorities = "CREATE_ATTRIBUTE")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should create attribute and return 201 with location header")
        void shouldCreateAttribute() throws Exception {
            var request = new AttributeCreateRequest(
                    ATTRIBUTE_NAME,
                    ATTRIBUTE_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string(HttpHeaders.LOCATION, BASE_URL + "/" + NEW_ATTRIBUTE_ID));

            AttributeResponse created = attributeService.read(NEW_ATTRIBUTE_ID)
                    .orElseThrow();

            assertAll(
                    () -> assertThat(created.name()).isEqualTo(request.name()),
                    () -> assertThat(created.categoryId()).isEqualTo(request.categoryId()),
                    () -> assertThat(created.type()).isEqualTo(request.type()),
                    () -> assertThat(created.isFilterable()).isEqualTo(request.isFilterable()),
                    () -> assertThat(created.values().size()).isEqualTo(request.values().size())
            );
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_ATTRIBUTE")
        @DisplayName("Should return 400 when request validation fails")
        void shouldReturnBadRequest_whenValidationFails(Language language) throws Exception {
            var request = new AttributeCreateRequest(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString((request))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message")
                            .value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name")
                            .value(message("attribute.name.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.type")
                            .value(message("attribute.type.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.isFilterable")
                            .value(message("attribute.isFilterable.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.categoryId")
                            .value(message("category.id.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_ATTRIBUTE")
        @DisplayName("Should return 404 when category does not exist")
        void shouldReturnNotFound_whenCategoryDoesNotExist(Language language) throws Exception {
            var request = new AttributeCreateRequest(
                    ATTRIBUTE_NAME,
                    INVALID_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message")
                            .value(message("category.not.found", language.getLocale())));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            var request = new AttributeCreateRequest(
                    ATTRIBUTE_NAME,
                    ATTRIBUTE_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("PUT " + BASE_URL + "/{id}")
    class UpdateAttribute {

        @Test
        @WithMockUser(authorities = "UPDATE_ATTRIBUTE")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should update attribute and return 204")
        void shouldUpdateAttribute() throws Exception {
            var request = new AttributeUpdateRequest(
                    ATTRIBUTE_NAME,
                    ATTRIBUTE_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_ATTRIBUTE_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            AttributeResponse updatedAttribute = assertDoesNotThrow(() -> attributeService.read(EXISTING_ATTRIBUTE_ID)
                    .orElseThrow());

            assertAll("Verify updated attribute properties",
                    () -> assertThat(updatedAttribute.name()).isEqualTo(request.name()),
                    () -> assertThat(updatedAttribute.categoryId()).isEqualTo(request.categoryId()),
                    () -> assertThat(updatedAttribute.type()).isEqualTo(request.type()),
                    () -> assertThat(updatedAttribute.isFilterable()).isEqualTo(request.isFilterable()),
                    () -> assertThat(updatedAttribute.values().size()).isEqualTo(request.values().size())
            );
        }

        @Test
        @WithMockUser(authorities = "UPDATE_ATTRIBUTE")
        @DisplayName("Should return 404 when attribute ID doesn't exist")
        void shouldReturnNotFound_whenAttributeNotFound() throws Exception {
            var request = new AttributeUpdateRequest(
                    ATTRIBUTE_NAME,
                    ATTRIBUTE_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(put(BASE_URL + "/{id}", NON_EXISTENT_ATTRIBUTE_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_ATTRIBUTE")
        @DisplayName("Should return 400 when request validation fails")
        void shouldReturnBadRequest_whenValidationFails(Language language) throws Exception {
            var request = new AttributeUpdateRequest(null, null, null, null,
                    null, null);

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_ATTRIBUTE_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_ATTRIBUTE")
        @DisplayName("Should return 404 when category ID doesn't exist")
        void shouldReturnNotFound_whenCategoryNotFound(Language language) throws Exception {
            var request = new AttributeUpdateRequest(
                    ATTRIBUTE_NAME,
                    INVALID_CATEGORY_ID,
                    ATTRIBUTE_TYPE,
                    ATTRIBUTE_IS_FILTERABLE,
                    ATTRIBUTE_DESCRIPTION,
                    ATTRIBUTE_VALUES
            );

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_ATTRIBUTE_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(message("problemDetail.category.not.found", language.getLocale())));
        }

    }

    @Nested
    @DisplayName("DELETE " + BASE_URL + "/{id}")
    class DeleteAttribute {

        @Test
        @Disabled("TODO: Implement delete endpoint and tests")
        @DisplayName("Should delete attribute and return 204")
        void shouldDeleteAttribute() {
            // TODO: Implement delete test
        }

    }

    @Nested
    @DisplayName("GET " + BASE_URL + "/types")
    class GetAllAttributeTypes {

        @Test
        @WithMockUser(authorities = "READ_ALL_ATTRIBUTE_TYPES")
        @DisplayName("Should return all filtered attributes when search param matches")
        void shouldReturnAlAttributeTypes() throws Exception {
            mockMvc().perform(get(BASE_URL + "/types")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

    }

}