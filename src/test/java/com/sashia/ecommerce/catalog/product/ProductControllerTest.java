package com.sashia.ecommerce.catalog.product;

import com.jayway.jsonpath.JsonPath;
import com.sashia.ecommerce.catalog.product.dto.ProductCreateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductResponse;
import com.sashia.ecommerce.catalog.product.dto.ProductUpdateRequest;
import com.sashia.ecommerce.catalog.product.dto.ProductVariantRequest;
import com.sashia.ecommerce.media.MediaResourceType;
import com.sashia.ecommerce.media.MediaService;
import com.sashia.ecommerce.media.MediaStatus;
import com.sashia.ecommerce.media.internal.MediaRepository;
import com.sashia.ecommerce.ordering.order.CurrencyCode;
import com.sashia.shared.BaseControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Product Controller Tests")
class ProductControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/products";
    private static final long PRODUCT_CATEGORY_ID = 1L;
    private static final long NON_EXISTENT_ID = 0L;
    private static final long FIXTURE_VARIANT_ID = 1L; // belongs to fixture item 1, not to products created here

    private static final String ALL = "READ_ALL_PRODUCTS,READ_PRODUCT";

    @Autowired
    private ProductService productService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaRepository mediaRepository;

    @BeforeEach
    void cleanMedia() {
        mediaRepository.deleteAll();
    }

    // ============================== GET /products ==============================

    @Nested
    @DisplayName("GET " + BASE_URL)
    class GetAllProducts {

        @Test
        @WithMockUser(authorities = "READ_ALL_PRODUCTS")
        @DisplayName("Should return a page of products")
        void shouldReturnProducts() throws Exception {
            mockMvc().perform(get(BASE_URL))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.page.totalElements", greaterThanOrEqualTo(4)));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_PRODUCTS")
        @DisplayName("Should filter by title")
        void shouldFilterByTitle() throws Exception {
            mockMvc().perform(get(BASE_URL).param("name", "iphone"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(1))
                    .andExpect(jsonPath("$.content[0].title").value("Apple iPhone 16 Pro"));
        }

        @Test
        @WithMockUser(authorities = "READ_ALL_PRODUCTS")
        @DisplayName("Should return an empty page when nothing matches")
        void shouldReturnEmptyPage() throws Exception {
            mockMvc().perform(get(BASE_URL).param("name", "non-existent-term"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isEmpty())
                    .andExpect(jsonPath("$.page.totalElements").value(0));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            mockMvc().perform(get(BASE_URL)).andExpect(status().isForbidden());
        }
    }

    // ============================== GET /products/{id} ==============================

    @Nested
    @DisplayName("GET " + BASE_URL + "/{id}")
    class GetProductById {

        @Test
        @WithMockUser(authorities = "READ_PRODUCT")
        @DisplayName("Should return the product with its variants")
        void shouldReturnProduct() throws Exception {
            long id = createProduct(null);

            mockMvc().perform(get(BASE_URL + "/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.title").value("Test product"))
                    .andExpect(jsonPath("$.categoryId").value(PRODUCT_CATEGORY_ID))
                    .andExpect(jsonPath("$.variants.length()").value(1))
                    .andExpect(jsonPath("$.variants[0].quantity").value(5))
                    .andExpect(jsonPath("$.media").isEmpty());
        }

        @Test
        @WithMockUser(authorities = "READ_PRODUCT")
        @DisplayName("Should return 404 when product does not exist")
        void shouldReturnNotFound() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", NON_EXISTENT_ID))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            mockMvc().perform(get(BASE_URL + "/{id}", 1L)).andExpect(status().isForbidden());
        }
    }

    // ============================== POST /products ==============================

    @Nested
    @DisplayName("POST " + BASE_URL)
    class CreateProduct {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = {"CREATE_PRODUCT", "READ_PRODUCT"})
        @DisplayName("Should create a product and return 201 with location header")
        void shouldCreateProduct() throws Exception {
            String location = mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(createRequest(null))))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", startsWith(BASE_URL + "/")))
                    .andReturn().getResponse().getHeader("Location");

            mockMvc().perform(get(location))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value("Test product"))
                    .andExpect(jsonPath("$.variants.length()").value(1));
        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = {"CREATE_PRODUCT", "CREATE_MEDIA"})
        @DisplayName("Should change the listed draft media to SAVED")
        void shouldSaveDraftMedia() throws Exception {
            long first = uploadDraft();
            long second = uploadDraft();

            String location = mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(createRequest(List.of(second, first)))))
                    .andExpect(status().isCreated())
                    .andReturn().getResponse().getHeader("Location");

            long productId = Long.parseLong(location.substring(location.lastIndexOf('/') + 1));

            assertThat(mediaRepository.findById(first).orElseThrow().getStatus()).isEqualTo(MediaStatus.SAVED);
            assertThat(mediaRepository.findById(second).orElseThrow().getStatus()).isEqualTo(MediaStatus.SAVED);

            var media = mediaService.readByResource(MediaResourceType.PRODUCT, productId);
            assertThat(media).extracting("id").containsExactly(second, first);
        }

        @Test
        @WithMockUser(authorities = "CREATE_PRODUCT")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest() throws Exception {
            var request = new ProductCreateRequest(null, null, null, false, null, null, null);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
        }

        @Test
        @WithMockUser(authorities = "CREATE_PRODUCT")
        @DisplayName("Should return 404 when category does not exist")
        void shouldReturnNotFound_whenCategoryMissing() throws Exception {
            var request = new ProductCreateRequest("x", NON_EXISTENT_ID, null, false, null,
                    List.of(variant(null, 100, 1)), null);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(authorities = "CREATE_PRODUCT")
        @DisplayName("Should return 404 when a tag does not exist")
        void shouldReturnNotFound_whenTagMissing() throws Exception {
            var request = new ProductCreateRequest("x", PRODUCT_CATEGORY_ID, null, false, java.util.Set.of(9999L),
                    List.of(variant(null, 100, 1)), null);

            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            mockMvc().perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(createRequest(null))))
                    .andExpect(status().isForbidden());
        }
    }

    // ============================== PUT /products/{id} ==============================

    @Nested
    @DisplayName("PUT " + BASE_URL + "/{id}")
    class UpdateProduct {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = "UPDATE_PRODUCT")
        @DisplayName("Should update fields, change a variant and add a new one")
        void shouldUpdateProduct() throws Exception {
            long id = createProduct(null);
            long variantId = productService.read(id).orElseThrow().variants().getFirst().id();

            var request = new ProductUpdateRequest("Renamed", PRODUCT_CATEGORY_ID, "new description", true, null,
                    List.of(variant(variantId, 777, 9), variant(null, 888, 2)), null);

            mockMvc().perform(put(BASE_URL + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            ProductResponse updated = productService.read(id).orElseThrow();
            assertThat(updated.title()).isEqualTo("Renamed");
            assertThat(updated.description()).isEqualTo("new description");
            assertThat(updated.featured()).isTrue();
            assertThat(updated.variants()).hasSize(2);
        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = "UPDATE_PRODUCT")
        @DisplayName("Should remove variants that are not listed")
        void shouldRemoveUnlistedVariants() throws Exception {
            long id = productService.create(new ProductCreateRequest("Two variants", PRODUCT_CATEGORY_ID, null, false,
                    null, List.of(variant(null, 100, 1), variant(null, 200, 1)), null));
            long keep = productService.read(id).orElseThrow().variants().getFirst().id();

            var request = new ProductUpdateRequest("Two variants", PRODUCT_CATEGORY_ID, null, false, null,
                    List.of(variant(keep, 100, 1)), null);

            mockMvc().perform(put(BASE_URL + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            assertThat(productService.read(id).orElseThrow().variants()).hasSize(1);
        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = {"UPDATE_PRODUCT", "CREATE_MEDIA"})
        @DisplayName("Should keep media when mediaIds is null and remove it when the list is empty")
        void shouldSyncMedia() throws Exception {
            long mediaId = uploadDraft();
            long id = createProduct(List.of(mediaId));

            mockMvc().perform(put(BASE_URL + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(updateRequest(null))))
                    .andExpect(status().isNoContent());
            assertThat(mediaService.readByResource(MediaResourceType.PRODUCT, id)).hasSize(1);

            mockMvc().perform(put(BASE_URL + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(updateRequest(List.of()))))
                    .andExpect(status().isNoContent());
            assertThat(mediaService.readByResource(MediaResourceType.PRODUCT, id)).isEmpty();
            assertThat(mediaRepository.findById(mediaId)).isEmpty();
        }

        @Test
        @WithMockUser(authorities = "UPDATE_PRODUCT")
        @DisplayName("Should return 404 when a variant belongs to another product")
        void shouldReturnNotFound_whenVariantIsForeign() throws Exception {
            long id = createProduct(null);

            var request = new ProductUpdateRequest("x", PRODUCT_CATEGORY_ID, null, false, null,
                    List.of(variant(FIXTURE_VARIANT_ID, 100, 1)), null);

            mockMvc().perform(put(BASE_URL + "/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(authorities = "UPDATE_PRODUCT")
        @DisplayName("Should return 404 when product does not exist")
        void shouldReturnNotFound() throws Exception {
            mockMvc().perform(put(BASE_URL + "/{id}", NON_EXISTENT_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(updateRequest(null))))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser(authorities = "UPDATE_PRODUCT")
        @DisplayName("Should return 400 when required fields are missing")
        void shouldReturnBadRequest() throws Exception {
            var request = new ProductUpdateRequest(null, null, null, false, null, null, null);

            mockMvc().perform(put(BASE_URL + "/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            mockMvc().perform(put(BASE_URL + "/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper().writeValueAsString(updateRequest(null))))
                    .andExpect(status().isForbidden());
        }
    }

    // ============================== DELETE /products/{id} ==============================

    @Nested
    @DisplayName("DELETE " + BASE_URL + "/{id}")
    class DeleteProduct {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @WithMockUser(authorities = {"DELETE_PRODUCT", "READ_PRODUCT", "CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should delete the product and its media")
        void shouldDeleteProduct() throws Exception {
            long mediaId = uploadDraft();
            long id = createProduct(List.of(mediaId));

            mockMvc().perform(delete(BASE_URL + "/{id}", id)).andExpect(status().isNoContent());

            mockMvc().perform(get(BASE_URL + "/{id}", id)).andExpect(status().isNotFound());
            assertThat(mediaRepository.findById(mediaId)).isEmpty();
        }

        @Test
        @WithMockUser(authorities = "DELETE_PRODUCT")
        @DisplayName("Should return 404 when product does not exist")
        void shouldReturnNotFound() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", NON_EXISTENT_ID)).andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            mockMvc().perform(delete(BASE_URL + "/{id}", 1L)).andExpect(status().isForbidden());
        }
    }

    // ============================== FIXTURES ==============================

    private static ProductVariantRequest variant(Long id, long price, int stock) {
        return new ProductVariantRequest(id, BigDecimal.valueOf(price), CurrencyCode.IRR, stock);
    }

    private static ProductCreateRequest createRequest(List<Long> mediaIds) {
        return new ProductCreateRequest("Test product", PRODUCT_CATEGORY_ID, "lorem ipsum", false, null,
                List.of(variant(null, 50_000, 5)), mediaIds);
    }

    private static ProductUpdateRequest updateRequest(List<Long> mediaIds) {
        return new ProductUpdateRequest("Test product", PRODUCT_CATEGORY_ID, "lorem ipsum", false, null,
                List.of(variant(null, 50_000, 5)), mediaIds);
    }

    /**
     * Created through the service so the test does not depend on the endpoint under test.
     */
    private long createProduct(List<Long> mediaIds) {
        return productService.create(createRequest(mediaIds));
    }

    private long uploadDraft() throws Exception {
        BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);

        String body = mockMvc().perform(multipart("/media/drafts")
                        .file(new MockMultipartFile("files", "photo.png", "image/png", out.toByteArray())))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$[0].id")).longValue();
    }
}