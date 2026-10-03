package com.sashia.ecommerce.media.internal;

import com.sashia.shared.exception.InvalidResourceException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Decides whether an uploaded file is acceptable, before anything is written to the storage.
 */
@Component
class MediaInspector {

    record Inspection(MediaFormat format, Map<String, Object> metadata) {
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

        MediaFormat format = detectFormat(file);
        Map<String, Object> metadata = new LinkedHashMap<>();

        if (format.inspectable())
            readDimensions(file, metadata);

        return new Inspection(format, metadata.isEmpty() ? null : metadata);
    }

    // =============================== HELPERS ===============================

    private MediaFormat detectFormat(MultipartFile file) {
        byte[] header = new byte[MediaFormat.HEADER_SIZE];
        int read;
        try (InputStream in = file.getInputStream()) {
            read = in.readNBytes(header, 0, header.length);
        } catch (IOException e) {
            throw new InvalidResourceException("media.file.corrupted");
        }
        return MediaFormat.detect(header, read)
                .orElseThrow(() -> new InvalidResourceException("media.type.unsupported"));
    }

    /** Reads only the header of the image (no pixel decoding), so it is cheap and safe against bombs. */
    private void readDimensions(MultipartFile file, Map<String, Object> metadata) {
        try (InputStream in = file.getInputStream();
             ImageInputStream iis = ImageIO.createImageInputStream(in)) {

            Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
            if (!readers.hasNext())
                throw new InvalidResourceException("media.file.corrupted");

            ImageReader reader = readers.next();
            try {
                reader.setInput(iis, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);

                if (width <= 0 || height <= 0)
                    throw new InvalidResourceException("media.file.corrupted");
                if ((long) width * height > properties.maxPixels())
                    throw new InvalidResourceException("media.image.too.large");

                metadata.put("width", width);
                metadata.put("height", height);
            } finally {
                reader.dispose();
            }
        } catch (IOException | RuntimeException e) {
            if (e instanceof InvalidResourceException invalid)
                throw invalid;
            throw new InvalidResourceException("media.file.corrupted");
        }
    }

}
