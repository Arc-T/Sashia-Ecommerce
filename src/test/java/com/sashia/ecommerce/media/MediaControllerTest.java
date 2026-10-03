package com.sashia.ecommerce.media;

import com.jayway.jsonpath.JsonPath;
import com.sashia.ecommerce.media.internal.MediaProperties;
import com.sashia.ecommerce.media.internal.MediaRepository;
import com.sashia.shared.BaseControllerTest;
import com.sashia.shared.WithSashiaUser;
import com.sashia.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Media Controller Tests")
class MediaControllerTest extends BaseControllerTest {

    private static final String BASE_URL = "/media";
    private static final long OTHER_USER_ID = 99L;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaProperties properties;

    @Autowired
    private MediaService mediaService;

    @BeforeEach
    void cleanUp() {
        mediaRepository.deleteAll();
    }

    // ============================== HELPERS ==============================

    private static byte[] png() throws Exception {
        BufferedImage image = new BufferedImage(4, 3, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        return out.toByteArray();
    }

    private static MockMultipartFile pngFile() throws Exception {
        return new MockMultipartFile("files", "photo.png", "image/png", png());
    }

    private ResultActions upload(MockMultipartFile... files) throws Exception {
        var request = multipart(BASE_URL + "/drafts");
        for (MockMultipartFile file : files)
            request.file(file);
        return mockMvc().perform(request);
    }

    /** Uploads one PNG and returns the media id. */
    private long uploadOne() throws Exception {
        String body = upload(pngFile()).andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return ((Number) JsonPath.read(body, "$[0].id")).longValue();
    }

    private Path fileOf(Media media) {
        return properties.root().toAbsolutePath().normalize().resolve(media.getStorageKey());
    }

    private String ids(long... ids) {
        StringBuilder sb = new StringBuilder("{\"mediaIds\":[");
        for (int i = 0; i < ids.length; i++)
            sb.append(i == 0 ? "" : ",").append(ids[i]);
        return sb.append("]}").toString();
    }

    private ResultActions sync(String type, long resourceId, String body) throws Exception {
        return mockMvc().perform(put(BASE_URL + "/resources/" + type + "/" + resourceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private Media otherUsersDraft() {
        Media media = new Media();
        media.setSlug("other-user-draft.png");
        media.setStorageKey("2026/01/other-user-draft.png");
        media.setChecksum("0".repeat(64));
        media.setSize(1L);
        media.setMimeType("image/png");
        media.setExtension("png");
        media.setStatus(MediaStatus.DRAFT);
        media.setOwnerId(OTHER_USER_ID);
        return mediaRepository.save(media);
    }

    // ============================== UPLOAD ==============================

    @Nested
    @DisplayName("POST " + BASE_URL + "/drafts")
    class UploadDrafts {

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should store the file as a draft and return its data")
        void shouldCreateDraft() throws Exception {
            upload(pngFile())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].status").value("DRAFT"))
                    .andExpect(jsonPath("$[0].mimeType").value("image/png"))
                    .andExpect(jsonPath("$[0].type").value("IMAGE"))
                    .andExpect(jsonPath("$[0].width").value(4))
                    .andExpect(jsonPath("$[0].height").value(3))
                    .andExpect(jsonPath("$[0].url", startsWith("/media/files/")));

            Media saved = mediaRepository.findAll().getFirst();
            assertThat(saved.getOwnerId()).isEqualTo(1L);
            assertThat(saved.getChecksum()).hasSize(64);
            assertThat(fileOf(saved)).exists().hasSize(saved.getSize());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should trust the content, not the file name or content type sent by the client")
        void shouldIgnoreClientNameAndType() throws Exception {
            MockMultipartFile file = new MockMultipartFile("files", "../../evil.php", "text/html", png());

            upload(file)
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$[0].mimeType").value("image/png"))
                    .andExpect(jsonPath("$[0].extension").value("png"))
                    .andExpect(jsonPath("$[0].fileName").value("evil.php"));

            assertThat(mediaRepository.findAll().getFirst().getSlug()).endsWith(".png");
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should reject a file that is not an image even if it is named .png")
        void shouldRejectDisguisedFile() throws Exception {
            MockMultipartFile html = new MockMultipartFile("files", "photo.png", "image/png",
                    "<script>alert(1)</script>".getBytes(StandardCharsets.UTF_8));

            upload(html).andExpect(status().isUnprocessableContent());
            assertThat(mediaRepository.count()).isZero();
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should reject an empty file")
        void shouldRejectEmptyFile() throws Exception {
            upload(new MockMultipartFile("files", "empty.png", "image/png", new byte[0]))
                    .andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should store nothing when one of the files is invalid")
        void shouldBeAllOrNothing() throws Exception {
            MockMultipartFile bad = new MockMultipartFile("files", "bad.png", "image/png", "nope".getBytes(StandardCharsets.UTF_8));

            upload(pngFile(), bad).andExpect(status().isUnprocessableContent());
            assertThat(mediaRepository.count()).isZero();
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should reject resourceType without resourceId")
        void shouldRejectHalfResource() throws Exception {
            mockMvc().perform(multipart(BASE_URL + "/drafts").file(pngFile()).param("resourceType", "PRODUCT"))
                    .andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should bind a draft to an existing resource")
        void shouldBindDraftToResource() throws Exception {
            mockMvc().perform(multipart(BASE_URL + "/drafts").file(pngFile())
                            .param("resourceType", "PRODUCT").param("resourceId", "5"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$[0].resourceType").value("PRODUCT"))
                    .andExpect(jsonPath("$[0].resourceId").value(5));

            mockMvc().perform(get(BASE_URL + "/drafts").param("resourceType", "PRODUCT").param("resourceId", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));

            mockMvc().perform(get(BASE_URL + "/drafts").param("resourceType", "PRODUCT").param("resourceId", "6"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @WithSashiaUser
        @DisplayName("Should return 403 when user lacks authority")
        void shouldReturnForbidden() throws Exception {
            upload(pngFile()).andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Should return 401 for anonymous users")
        void shouldReturnUnauthorized() throws Exception {
            upload(pngFile()).andExpect(status().isUnauthorized());
        }

    }

    // ============================== DRAFT VISIBILITY ==============================

    @Nested
    @DisplayName("Draft visibility")
    class DraftVisibility {

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Owner can download the draft, with safe headers and no caching")
        void ownerCanDownloadDraft() throws Exception {
            uploadOne();
            Media media = mediaRepository.findAll().getFirst();

            mockMvc().perform(get(BASE_URL + "/files/" + media.getSlug()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"))
                    .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                    .andExpect(header().string("Cache-Control", containsString("no-store")))
                    .andExpect(content().bytes(Files.readAllBytes(fileOf(media))));
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Anonymous users cannot see a draft (404, existence is not revealed)")
        void anonymousCannotDownloadDraft() throws Exception {
            uploadOne();
            Media media = mediaRepository.findAll().getFirst();

            mockMvc().perform(get(BASE_URL + "/files/" + media.getSlug()).with(anonymous()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("A user cannot read or download the draft of another user")
        void cannotSeeOtherUsersDraft() throws Exception {
            Media other = otherUsersDraft();

            mockMvc().perform(get(BASE_URL + "/" + other.getId())).andExpect(status().isNotFound());
            mockMvc().perform(get(BASE_URL + "/files/" + other.getSlug())).andExpect(status().isNotFound());
            mockMvc().perform(delete(BASE_URL + "/" + other.getId())).andExpect(status().isNotFound());
            mockMvc().perform(get(BASE_URL + "/drafts")).andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @WithSashiaUser(authorities = "READ_ALL_MEDIA")
        @DisplayName("A manager can read the draft of another user")
        void managerCanSeeOtherUsersDraft() throws Exception {
            Media other = otherUsersDraft();

            mockMvc().perform(get(BASE_URL + "/" + other.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("DRAFT"));
        }

    }

    // ============================== SAVE (SYNC) ==============================

    @Nested
    @DisplayName("PUT " + BASE_URL + "/resources/{type}/{id}")
    class Sync {

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should save drafts in the requested order and make them public")
        void shouldSaveDraftsInOrder() throws Exception {
            long first = uploadOne();
            long second = uploadOne();

            sync("PRODUCT", 7, ids(second, first))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(second))
                    .andExpect(jsonPath("$[0].status").value("SAVED"))
                    .andExpect(jsonPath("$[0].displayOrder").value(0))
                    .andExpect(jsonPath("$[1].id").value(first))
                    .andExpect(jsonPath("$[1].displayOrder").value(1));

            // public, no login needed
            mockMvc().perform(get(BASE_URL + "/resources/PRODUCT/7").with(anonymous()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(second));

            Media saved = mediaRepository.findById(second).orElseThrow();
            mockMvc().perform(get(BASE_URL + "/files/" + saved.getSlug()).with(anonymous()))
                    .andExpect(status().isOk())
                    .andExpect(header().string("Cache-Control", containsString("immutable")))
                    .andExpect(header().string("Cache-Control", containsString("public")));
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should delete saved media that are no longer listed, files included")
        void shouldRemoveUnlistedMedia() throws Exception {
            long keep = uploadOne();
            long drop = uploadOne();
            sync("PRODUCT", 7, ids(keep, drop)).andExpect(status().isOk());
            Path droppedFile = fileOf(mediaRepository.findById(drop).orElseThrow());
            assertThat(droppedFile).exists();

            sync("PRODUCT", 7, ids(keep)).andExpect(status().isOk());

            assertThat(mediaRepository.findById(drop)).isEmpty();
            assertThat(droppedFile).doesNotExist();
            assertThat(mediaRepository.findById(keep)).isPresent();
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should refuse media that is saved for another resource")
        void shouldRefuseMediaOfAnotherResource() throws Exception {
            long id = uploadOne();
            sync("PRODUCT", 7, ids(id)).andExpect(status().isOk());

            sync("PRODUCT", 8, ids(id)).andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should refuse a draft that was uploaded for another resource")
        void shouldRefuseDraftBoundToAnotherResource() throws Exception {
            mockMvc().perform(multipart(BASE_URL + "/drafts").file(pngFile())
                    .param("resourceType", "PRODUCT").param("resourceId", "5")).andExpect(status().isCreated());
            Media draft = mediaRepository.findAll().getFirst();

            sync("PRODUCT", 6, ids(draft.getId())).andExpect(status().isUnprocessableContent());
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Should reject duplicated ids and unknown ids")
        void shouldRejectBadIds() throws Exception {
            long id = uploadOne();

            sync("PRODUCT", 7, ids(id, id)).andExpect(status().isUnprocessableContent());
            sync("PRODUCT", 7, ids(123456)).andExpect(status().isNotFound());
        }

        @Test
        @WithSashiaUser(authorities = "UPDATE_MEDIA")
        @DisplayName("A manager (UPDATE_MEDIA) can save the draft of another user")
        void managerCanSaveOtherUsersDraft() throws Exception {
            Media other = otherUsersDraft();

            sync("PRODUCT", 7, ids(other.getId())).andExpect(status().isOk());
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Service level: a normal user cannot save the draft of another user")
        void userCannotSaveOtherUsersDraft() {
            Media other = otherUsersDraft();

            assertThatThrownBy(() -> mediaService.sync(MediaResourceType.PRODUCT, 7L, List.of(other.getId())))
                    .isInstanceOf(ResourceNotFoundException.class);
            assertThat(mediaRepository.findById(other.getId()).orElseThrow().getStatus()).isEqualTo(MediaStatus.DRAFT);
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Should return 403 without UPDATE_MEDIA")
        void shouldReturnForbidden() throws Exception {
            sync("PRODUCT", 7, ids()).andExpect(status().isForbidden());
        }

    }

    // ============================== UPDATE / DELETE / SEARCH ==============================

    @Nested
    @DisplayName("PUT / DELETE / GET " + BASE_URL)
    class UpdateDeleteSearch {

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Owner can edit the description of a draft")
        void shouldUpdateDraft() throws Exception {
            long id = uploadOne();

            mockMvc().perform(put(BASE_URL + "/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"description\":\"front view\",\"displayOrder\":3}"))
                    .andExpect(status().isNoContent());

            Media media = mediaRepository.findById(id).orElseThrow();
            assertThat(media.getDescription()).isEqualTo("front view");
            assertThat(media.getDisplayOrder()).isEqualTo(3);
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Owner can delete a draft and its file is removed")
        void shouldDeleteOwnDraft() throws Exception {
            long id = uploadOne();
            Path file = fileOf(mediaRepository.findById(id).orElseThrow());
            assertThat(file).exists();

            mockMvc().perform(delete(BASE_URL + "/" + id)).andExpect(status().isNoContent());

            assertThat(mediaRepository.findById(id)).isEmpty();
            assertThat(file).doesNotExist();
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA"})
        @DisplayName("Saved media cannot be deleted without DELETE_MEDIA")
        void shouldProtectSavedMedia() throws Exception {
            long id = uploadOne();
            sync("PRODUCT", 7, ids(id)).andExpect(status().isOk());

            mockMvc().perform(delete(BASE_URL + "/" + id)).andExpect(status().isForbidden());
            assertThat(mediaRepository.findById(id)).isPresent();
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "UPDATE_MEDIA", "DELETE_MEDIA"})
        @DisplayName("DELETE_MEDIA can delete saved media")
        void shouldDeleteSavedMedia() throws Exception {
            long id = uploadOne();
            sync("PRODUCT", 7, ids(id)).andExpect(status().isOk());

            mockMvc().perform(delete(BASE_URL + "/" + id)).andExpect(status().isNoContent());
            assertThat(mediaRepository.findById(id)).isEmpty();
        }

        @Test
        @WithSashiaUser(authorities = {"CREATE_MEDIA", "READ_ALL_MEDIA"})
        @DisplayName("Should search with filters and pagination")
        void shouldSearch() throws Exception {
            uploadOne();
            uploadOne();
            otherUsersDraft();

            mockMvc().perform(get(BASE_URL).param("size", "2").param("sort", "id,desc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.length()").value(2))
                    .andExpect(jsonPath("$.page.totalElements").value(3));

            mockMvc().perform(get(BASE_URL).param("ownerId", String.valueOf(OTHER_USER_ID)))
                    .andExpect(jsonPath("$.content.length()").value(1));

            mockMvc().perform(get(BASE_URL).param("mimeType", "image/").param("status", "DRAFT"))
                    .andExpect(jsonPath("$.page.totalElements").value(3));
        }

        @Test
        @WithSashiaUser(authorities = "CREATE_MEDIA")
        @DisplayName("Search needs READ_ALL_MEDIA")
        void searchShouldBeForbidden() throws Exception {
            mockMvc().perform(get(BASE_URL)).andExpect(status().isForbidden());
        }

    }

}
