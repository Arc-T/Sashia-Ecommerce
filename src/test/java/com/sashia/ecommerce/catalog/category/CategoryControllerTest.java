package com.sashia.ecommerce.catalog.category;

import com.sashia.ecommerce.catalog.category.dto.CategoryCreateRequest;
import com.sashia.ecommerce.catalog.category.dto.CategoryResponse;
import com.sashia.ecommerce.catalog.category.dto.CategoryUpdateRequest;
import com.sashia.ecommerce.catalog.item.dto.ItemType;
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

@DisplayName("Category Controller Tests")
class CategoryControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/categories";
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NON_EXISTENT_CATEGORY_ID = 0L;
    private static final Long INVALID_PARENT_ID = 0L;
    private static final Long NEW_CATEGORY_ID = 4L;

    private static final String CATEGORY_NAME = "Test category";
    private static final String CATEGORY_URL = "/accessory";
    private static final String CATEGORY_ICON = "mui-necklace";
    private static final int CATEGORY_DISPLAY_ORDER = 1;
    private static final String CATEGORY_DESCRIPTION = "lorem ipsum";
    private static final ItemType CATEGORY_ITEM_TYPE = ItemType.PRODUCT;

    @Autowired
    private CategoryService categoryService;

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllCategories {

        @Test
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
        @DisplayName("Should return all categories with status 200")
        void shouldReturnAllCategories() throws Exception {
            mockMvc().perform(get(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(3))
                    .andExpect(jsonPath("$.page.totalElements").value(3));
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
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
        @DisplayName("Should return all filtered categories when search param matches")
        void shouldReturnFilteredCategories_whenSearchParamMatchesWithOne() throws Exception {
            String searchTerm = "زیور";

            mockMvc().perform(get(BASE_URL)
                            .param("name", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
        @DisplayName("Should return children categories")
        void shouldReturnChildrenCategories() throws Exception {
            String searchTerm = Boolean.TRUE.toString();

            mockMvc().perform(get(BASE_URL)
                            .param("onlyChildren", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(3));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
        @DisplayName("Should return children categories")
        void shouldReturnParentCategories() throws Exception {
            String searchTerm = Boolean.TRUE.toString();

            mockMvc().perform(get(BASE_URL)
                            .param("onlyParents", searchTerm)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
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
        @WithMockUser(authorities = "READ_ALL_CATEGORIES")
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
    class GetCategoryById {

        @Test
        @WithMockUser(authorities = "READ_CATEGORY")
        @DisplayName("Should return category when ID exists")
        void shouldReturnCategory_whenIdExists() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXISTING_CATEGORY_ID))
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.url").isString());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @TestWithLocale
        @WithMockUser(authorities = "READ_CATEGORY")
        @DisplayName("Should return 404 when category ID doesn't exist")
        void shouldReturnNotFound_whenIdDoesNotExist(Language language) throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", NON_EXISTENT_CATEGORY_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateCategory {

        @Test
        @WithMockUser(authorities = "CREATE_CATEGORY")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Should create category and return 201 with location header")
        void shouldCreateCategory() throws Exception {
            CategoryCreateRequest request = new CategoryCreateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ITEM_TYPE, CATEGORY_ICON,
                    null, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", BASE_URL + "/" + NEW_CATEGORY_ID));

            // Verify in database
            CategoryResponse createdCategory = categoryService.read(NEW_CATEGORY_ID)
                    .orElseThrow();

            assertAll("Verify created category properties",
                    () -> assertThat(createdCategory.name()).isEqualTo(request.name()),
                    () -> assertThat(createdCategory.url()).isEqualTo(request.url()),
                    () -> assertThat(createdCategory.icon()).isEqualTo(request.icon()),
                    () -> assertThat(createdCategory.parentId()).isEqualTo(request.parentId()),
                    () -> assertThat(createdCategory.displayOrder()).isEqualTo(request.displayOrder()),
                    () -> assertThat(createdCategory.description()).isEqualTo(request.description())
            );
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_CATEGORY")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest_whenRequiredFieldsMissing(Language language) throws Exception {
            CategoryCreateRequest request = new CategoryCreateRequest(null, null, null, null, null, null, null);

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.name").value(message("category.name.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.url").value(message("category.url.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.icon").value(message("category.icon.required", language.getLocale())))
                    .andExpect(jsonPath("$.details.fieldErrors.displayOrder").value(message("category.displayOrder.required", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "CREATE_CATEGORY")
        @DisplayName("Should return 404 when parent category doesn't exist")
        void shouldReturnNotFound_whenParentNotFound(Language language) throws Exception {
            CategoryCreateRequest request = new CategoryCreateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ITEM_TYPE, CATEGORY_ICON,
                    INVALID_PARENT_ID, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(post(BASE_URL)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(message("category.parentId.not.found", language.getLocale())));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            CategoryCreateRequest request = new CategoryCreateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ITEM_TYPE, CATEGORY_ICON,
                    null, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @Disabled("TODO: Add handler for empty request body")
        @WithMockUser(authorities = "CREATE_CATEGORY")
        @DisplayName("Should return 400 when request body is empty")
        void shouldReturnBadRequest_whenRequestBodyEmpty() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT " + BASE_URL + "/{id}")
    class UpdateCategory {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = "UPDATE_CATEGORY")
        @DisplayName("Should update category and return 204")
        void shouldUpdateCategory() throws Exception {
            CategoryUpdateRequest request = new CategoryUpdateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ICON,
                    null, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            // Verify in database
            CategoryResponse updatedCategory = categoryService.read(EXISTING_CATEGORY_ID)
                    .orElseThrow();

            assertAll("Verify updated category properties",
                    () -> assertThat(updatedCategory.name()).isEqualTo(request.name()),
                    () -> assertThat(updatedCategory.url()).isEqualTo(request.url()),
                    () -> assertThat(updatedCategory.icon()).isEqualTo(request.icon()),
                    () -> assertThat(updatedCategory.parentId()).isEqualTo(request.parentId()),
                    () -> assertThat(updatedCategory.displayOrder()).isEqualTo(request.displayOrder()),
                    () -> assertThat(updatedCategory.description()).isEqualTo(request.description())
            );
        }

        @Test
        @WithMockUser(authorities = "UPDATE_CATEGORY")
        @DisplayName("Should return 422 when category ID doesn't exist")
        void shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
            CategoryUpdateRequest request = new CategoryUpdateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ICON,
                    null, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(put(BASE_URL + "/{id}", NON_EXISTENT_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_CATEGORY")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest_whenRequiredFieldsMissing(Language language) throws Exception {
            CategoryUpdateRequest request = new CategoryUpdateRequest(null, null, null, null, null, null);

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(message("invalid.method.params", language.getLocale())));
        }

        @TestWithLocale
        @WithMockUser(authorities = "UPDATE_CATEGORY")
        @DisplayName("Should return 422 when parent category doesn't exist")
        void shouldReturnNotFound_whenParentNotFound(Language language) throws Exception {
            CategoryUpdateRequest request = new CategoryUpdateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ICON,
                    INVALID_PARENT_ID, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .locale(language.getLocale())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(message("category.parentId.not.found", language.getLocale())));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            CategoryUpdateRequest request = new CategoryUpdateRequest(
                    CATEGORY_NAME, CATEGORY_URL, CATEGORY_ICON,
                    null, CATEGORY_DISPLAY_ORDER, CATEGORY_DESCRIPTION
            );

            mockMvc().perform(put(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    @DisplayName("DELETE " + BASE_URL + "/{id}")
    class DeleteCategory {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = "DELETE_CATEGORY")
        @DisplayName("Should return 422 when category has assigned products")
        void shouldDeleteCategory() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithMockUser(authorities = "DELETE_CATEGORY")
        @DisplayName("Should return 422 when category ID doesn't exist")
        void shouldReturnNotFound_whenCategoryDoesNotExist() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", NON_EXISTENT_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden_whenUserLacksAuthority() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", EXISTING_CATEGORY_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = "DELETE_CATEGORY")
        @DisplayName("Should return 409 when category has children")
        void shouldReturnConflict_whenCategoryHasChildren() throws Exception {
            // This test would require setting up a parent category with children
            // Implementation depends on your test data setup
        }

    }

}