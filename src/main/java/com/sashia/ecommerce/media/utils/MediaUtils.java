package com.sashia.ecommerce.media.utils;

import com.sashia.ecommerce.media.dto.MediaRequest;
import com.sashia.ecommerce.media.dto.MediaResponse;
import com.sashia.ecommerce.media.dto.MediaTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MediaUtils {

    private static final Path ROOT = Paths.get("uploads");
    private static final Path DRAFT = ROOT.resolve("draft");
    private static final Path PRODUCTS = ROOT.resolve("products");

    private static final Logger log = LoggerFactory.getLogger(MediaUtils.class);

    private MediaUtils() {
    }

    /* ============================== LIST ============================== */

    private static List<MediaResponse> list(Path dir) {
        ensureDir(dir);
        try (Stream<Path> s = Files.list(dir)) {
            return s.filter(Files::isRegularFile)
                    .sorted()
                    .map(p -> new MediaResponse(p.getFileName().toString()))
                    .toList();
        } catch (IOException e) {
            throw fail("LIST_MEDIA_FAILED", dir, e);
        }
    }

    public static List<MediaResponse> listDraft() {
        return list(DRAFT);
    }

    public static List<MediaResponse> listProductDraft(long productId) {
        return list(productDraftDir(productId));
    }

    /* ============================== SAVE ============================== */

    public static List<MediaResponse> draft(MultipartFile[] files) {
        return save(files, DRAFT);
    }

    public static List<MediaResponse> draft(long productId, MultipartFile[] files) {
        return save(files, productDraftDir(productId));
    }

    private static List<MediaResponse> save(MultipartFile[] files, Path dir) {
        ensureDir(dir);
        return Stream.of(files)
                .map(f -> saveOne(f, dir))
                .toList();
    }

    private static MediaResponse saveOne(MultipartFile file, Path dir) {
        String ext = ext(file.getOriginalFilename());
        String name = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
        Path target = dir.resolve(name);

        try {
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return new MediaResponse(name);
        } catch (IOException e) {
            throw fail("SAVE_MEDIA_FAILED", target, e);
        }
    }

    /* ============================== MOVE ============================== */

    public static List<MediaRequest> moveAllDraftsToProduct(long productId, boolean isUpdate) {
        Path targetDir = productDir(productId);
        ensureDir(targetDir);
        if (listProductDraft(productId).isEmpty())
            return List.of();
        try (Stream<Path> s = Files.list(isUpdate ? productDraftDir(productId) : DRAFT)) {
            List<Path> files = s.filter(Files::isRegularFile).sorted().toList();

            return IntStream.range(0, files.size())
                    .mapToObj(i -> moveDraft(files.get(i), targetDir, productId, i))
                    .toList();

        } catch (IOException e) {
            throw fail("MOVE_DRAFT_FAILED", targetDir, e);
        }
    }

    private static MediaRequest moveDraft(Path src, Path targetDir, long productId, int order) {
        Path target = targetDir.resolve(src.getFileName());
        try {
            Files.move(src, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw fail("MOVE_MEDIA_FAILED", target, e);
        }

        String name = target.getFileName().toString();
        return new MediaRequest(
                productId,
                name,
                MediaTypeEnum.IMAGE.name(),
                ext(name),
                LocalDateTime.now()
        );
    }

    /* ============================== DELETE ============================== */

    public static void deleteDraft(String filename) {
        delete(DRAFT.resolve(filename));
    }

    public static void deleteProductMedia(long productId, String filename) {
        delete(productDir(productId).resolve(filename));
    }

    public static void deleteDraftProductMedia(long productId, String filename) {
        delete(productDraftDir(productId).resolve(filename));
    }

    public static void deleteProductFolder(long productId) {
        deleteDir(productDir(productId));
    }

    private static void delete(Path file) {
        if (Files.notExists(file)) throw new UtilsException("FILE.NOT.FOUND");
        try {
            Files.delete(file);
        } catch (IOException e) {
            throw fail("DELETE_MEDIA_FAILED", file, e);
        }
    }

    private static void deleteDir(Path dir) {
        if (Files.notExists(dir)) return;
        try (Stream<Path> s = Files.walk(dir)) {
            s.sorted(Comparator.reverseOrder()).forEach(MediaUtils::silentDelete);
        } catch (IOException e) {
            throw fail("DELETE_DIR_FAILED", dir, e);
        }
    }

    private static void silentDelete(Path p) {
        try {
            Files.delete(p);
        } catch (IOException e) {
            throw fail("DELETE_FAILED", p, e);
        }
    }

    /* ============================== HELPERS ============================== */

    private static void ensureDir(Path dir) {
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            throw fail("CREATE_DIR_FAILED", dir, e);
        }
    }

    private static Path productDir(long productId) {
        return PRODUCTS.resolve(String.valueOf(productId));
    }

    private static Path productDraftDir(long productId) {
        return DRAFT.resolve("products").resolve(String.valueOf(productId));
    }

    private static String ext(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return (i > 0 && i < name.length() - 1) ? name.substring(i + 1) : "";
    }

    private static RuntimeException fail(String code, Path path, Exception e) {
        log.error("{}: {}", code, path, e);
        return new RuntimeException(code);
    }

}
