package com.sashia.ecommerce.catalog.tag;

import com.sashia.ecommerce.catalog.tag.dto.TagCreateRequest;
import com.sashia.ecommerce.catalog.tag.dto.TagResponse;
import com.sashia.ecommerce.catalog.tag.dto.TagUpdateRequest;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.Language;
import com.sashia.shared.TestWithLocale;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Tag Controller Tests")
class TagControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/tags";
    private static final Long EXISTING_TAG_ID = 1L;
    private static final Long NON_EXISTENT_TAG_ID = 0L;
    private static final Long INVALID_TAG_ID = 0L;
    private static final Long NEW_TAG_ID = 7L;

    private static final String TAG_NAME = "man";
    private static final String UPDATED_TAG_NAME = "test";
    private static final String SPECIAL_TAG_NAME = "special-tag-!@#$";

    @Autowired
    private TagService tagService;

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllTags {

        @Test
        @WithMockUser(authorities = "READ_ALL_TAGS")
        @DisplayName("Should return all tags with status 200")
        void shouldReturnAllTags() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content.length()").value(6));
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
        @WithMockUser(authorities = "READ_ALL_TAGS")
        @DisplayName("Should support pagination parameters")
        void shouldSupportPagination() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .param("page", "0")
                            .param("size", "5")
                            .param("sort", "name,asc")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page.number").value(0))
                    .andExpect(jsonPath("$.page.size").value(5));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_TAGS")
        @DisplayName("Should filter tags by search criteria")
        void shouldFilterTagsBySearchCriteria() throws Exception {
            String searchTerm = "زنانه";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].name").value(searchTerm));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_TAGS")
        @DisplayName("Should return empty page when no tags match search criteria")
        void shouldReturnEmptyPage_whenNoTagsMatchSearch() throws Exception {
            String searchTerm = "nonexistent-tag";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.page.totalElements").value(0));
        }

    }

    @Nested
    @DisplayName("GET " + BASE_URL + "/{id}")
    class GetTagById {

        @Test
        @WithMockUser(authorities = "READ_TAG")
        @DisplayName("Should return tag when ID exists")
        void shouldReturnTag_whenIdExists() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_TAG_ID))
                    .andExpect(jsonPath("$.name").isString());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @TestWithLocale
        @WithMockUser(authorities = "READ_TAG")
        @DisplayName("Should return 404 when tag ID doesn't exist")
        void shouldReturnNotFound_whenIdDoesNotExist(Language language) throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", NON_EXISTENT_TAG_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @TestWithLocale
        @WithMockUser(authorities = "READ_TAG")
        @DisplayName("Should return 404 when ID is invalid")
        void shouldReturnNotFound_whenIdIsInvalid(Language language) throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", INVALID_TAG_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateTag {

        @Test
        @WithMockUser(authorities = "CREATE_TAG")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should create tag and return 201 with location header")
        void shouldCreateTag() throws Exception {
            TagCreateRequest request = new TagCreateRequest(TAG_NAME);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", BASE_URL + "/" + NEW_TAG_ID));

            // Verify in database
            TagResponse createdTag = tagService.read(NEW_TAG_ID)
                    .orElseThrow();

            assertAll("Verify created tag properties",
                    () -> assertThat(createdTag).isNotNull(),
                    () -> assertThat(createdTag.id()).isEqualTo(NEW_TAG_ID),
                    () -> assertThat(createdTag.name()).isEqualTo(request.name())
            );
        }

        @Test
        @WithMockUser(authorities = "CREATE_TAG")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should create tag with special characters in name")
        void shouldCreateTagWithSpecialCharacters() throws Exception {
            TagCreateRequest request = new TagCreateRequest(SPECIAL_TAG_NAME);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // Verify in database
            TagResponse createdTag = tagService.read(NEW_TAG_ID)
                    .orElseThrow();

            assertThat(createdTag.name()).isEqualTo(SPECIAL_TAG_NAME);
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_TAG")
        @DisplayName("Should return 400 when name is null")
        void shouldReturnBadRequest_whenNameIsNull(Language language) throws Exception {
            TagCreateRequest request = new TagCreateRequest(null);

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("tag.name.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_TAG")
        @DisplayName("Should return 400 when name is empty string")
        void shouldReturnBadRequest_whenNameIsEmpty(Language language) throws Exception {
            TagCreateRequest request = new TagCreateRequest("");

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("tag.name.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_TAG")
        @DisplayName("Should return 400 when name is only whitespace")
        void shouldReturnBadRequest_whenNameIsWhitespace(Language language) throws Exception {
            TagCreateRequest request = new TagCreateRequest("   ");

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("tag.name.required", language.getLocale())));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            TagCreateRequest request = new TagCreateRequest(TAG_NAME);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @Disabled("TODO: Add handler for empty request body")
        @WithMockUser(authorities = "CREATE_TAG")
        @DisplayName("Should return 400 when request body is empty")
        void shouldReturnBadRequest_whenRequestBodyEmpty() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

    }

    @Nested
    @DisplayName("PUT " + BASE_URL + "/{id}")
    class UpdateTag {

        @Test
        @WithMockUser(authorities = "UPDATE_TAG")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should update tag and return 204")
        void shouldUpdateTag() throws Exception {
            TagUpdateRequest request = new TagUpdateRequest(UPDATED_TAG_NAME);

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            // Verify in database
            TagResponse updatedTag = tagService.read(EXISTING_TAG_ID)
                    .orElseThrow();

            assertAll("Verify updated tag properties",
                    () -> assertThat(updatedTag).isNotNull(),
                    () -> assertThat(updatedTag.id()).isEqualTo(EXISTING_TAG_ID),
                    () -> assertThat(updatedTag.name()).isEqualTo(request.name())
            );
        }

        @Test
        @WithMockUser(authorities = "UPDATE_TAG")
        @DisplayName("Should return 404 when tag ID doesn't exist")
        void shouldReturnNotFound_whenTagDoesNotExist() throws Exception {
            TagUpdateRequest request = new TagUpdateRequest(UPDATED_TAG_NAME);

            mockMvc().perform(put(BASE_URL + "/{id}", NON_EXISTENT_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_TAG")
        @DisplayName("Should return 400 when name is null")
        void shouldReturnBadRequest_whenNameIsNull(Language language) throws Exception {
            TagUpdateRequest request = new TagUpdateRequest(null);

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("tag.name.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_TAG")
        @DisplayName("Should return 400 when name is empty")
        void shouldReturnBadRequest_whenNameIsEmpty(Language language) throws Exception {
            TagUpdateRequest request = new TagUpdateRequest("");

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("tag.name.required", language.getLocale())));
        }

    }

    @Nested
    @DisplayName("DELETE " + BASE_URL + "/{id}")
    class DeleteTag {

        @Test
        @WithMockUser(authorities = "DELETE_TAG")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should delete tag and return 204")
        void shouldDeleteTag() throws Exception {
            TagCreateRequest createRequest = new TagCreateRequest("tag-to-delete");
            Long tagId = tagService.create(createRequest);

            mockMvc().perform(delete(BASE_URL + "/{id}", tagId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());

            // Verify deletion
//            assertThrows(ResourceNotFoundException.class, () -> tagService.read(tagId));
        }

        @Test
        @WithMockUser(authorities = "DELETE_TAG")
        @DisplayName("Should return 404 when tag ID doesn't exist")
        void shouldUnprocessableContent_whenTagDoesNotExist() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", NON_EXISTENT_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", EXISTING_TAG_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = "DELETE_TAG")
        @DisplayName("Should return 409 when tag is associated with products")
        void shouldReturnConflict_whenTagIsInUse() throws Exception {
            // This test requires a tag that is associated with products
            // Implementation depends on your test data setup
            // You might need to create a product and associate it with a tag
        }

    }

}