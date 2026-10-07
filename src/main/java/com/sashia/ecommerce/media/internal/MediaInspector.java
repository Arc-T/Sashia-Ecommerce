package com.sashia.ecommerce.media.internal;

import com.sashia.shared.exception.InvalidResourceException;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Decides whether an uploaded file is acceptable, before anything is written to the storage.
 */
@Component
class MediaInspector {

    private static final Tika TIKA = new Tika();

    /**
     * Allowed content types and the extension we store them with.
     */
    private static final Map<String, String> ALLOWED = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/gif", "gif",
            "image/webp", "webp");

    record Inspection(String mimeType, String extension, Map<String, Object> metadata) {
    }

    private final MediaProperties properties;

    MediaInspector(MediaProperties properties) {
        this.properties = properties;
    }

    Inspection inspect(MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new InvalidResourceException("media.file.empty");
        if (file.getSize() > properties.maxFileSize().toBytes())
            throw new InvalidResourceException("media.file.too.large");

        try (InputStream in = file.getInputStream()) {
            // Tika looks at the bytes only: the client's name and content type are never used.
            String mimeType = TIKA.detect(in);
            String extension = ALLOWED.get(mimeType);
            if (extension == null)
                throw new InvalidResourceException("media.type.unsupported");

            return new Inspection(mimeType, extension, readDimensions(file));
        } catch (IOException e) {
            throw new InvalidResourceException("media.file.corrupted");
        }
    }

    /**
     * Header only, no pixel decoding. WebP has no JDK reader, so it returns null.
     */
    private Map<String, Object> readDimensions(MultipartFile file) throws IOException {
        try (InputStream in = file.getInputStream();
             ImageInputStream iis = ImageIO.createImageInputStream(in)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext())
                return null;

            ImageReader reader = readers.next();
            try {
                reader.setInput(iis, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width <= 0 || height <= 0)
                    throw new InvalidResourceException("media.file.corrupted");
                if ((long) width * height > properties.maxPixels())
                    throw new InvalidResourceException("media.image.too.large");

                return Map.of("width", width, "height", height);
            } finally {
                reader.dispose();
            }
        }
    }
}