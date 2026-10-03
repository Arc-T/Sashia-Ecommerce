package com.sashia.ecommerce.media.internal;

import com.sashia.ecommerce.media.dto.MediaTypeCode;

import java.util.Optional;

/**
 * The only formats the module accepts. Detection is done on the first bytes of the file
 * (magic numbers); the client supplied file name and content type are ignored.
 *
 * <p>To allow a new format (video, pdf, ...), add a constant and its signature here.</p>
 */
enum MediaFormat {

    JPEG("image/jpeg", "jpg", MediaTypeCode.IMAGE, true),
    PNG("image/png", "png", MediaTypeCode.IMAGE, true),
    GIF("image/gif", "gif", MediaTypeCode.IMAGE, true),
    /* the JDK has no WebP reader, so dimensions are not extracted */
    WEBP("image/webp", "webp", MediaTypeCode.IMAGE, false);

    static final int HEADER_SIZE = 16;

    private final String mimeType;
    private final String extension;
    private final MediaTypeCode kind;
    private final boolean inspectable;

    MediaFormat(String mimeType, String extension, MediaTypeCode kind, boolean inspectable) {
        this.mimeType = mimeType;
        this.extension = extension;
        this.kind = kind;
        this.inspectable = inspectable;
    }

    String mimeType() {
        return mimeType;
    }

    String extension() {
        return extension;
    }

    MediaTypeCode kind() {
        return kind;
    }

    /**
     * true when the JDK can read the dimensions of this format.
     */
    boolean inspectable() {
        return inspectable;
    }

    static Optional<MediaFormat> detect(byte[] h, int length) {
        if (length >= 3 && (h[0] & 0xFF) == 0xFF && (h[1] & 0xFF) == 0xD8 && (h[2] & 0xFF) == 0xFF)
            return Optional.of(JPEG);

        if (length >= 8 && (h[0] & 0xFF) == 0x89 && h[1] == 'P' && h[2] == 'N' && h[3] == 'G'
                && h[4] == 0x0D && h[5] == 0x0A && h[6] == 0x1A && h[7] == 0x0A)
            return Optional.of(PNG);

        if (length >= 6 && h[0] == 'G' && h[1] == 'I' && h[2] == 'F' && h[3] == '8'
                && (h[4] == '7' || h[4] == '9') && h[5] == 'a')
            return Optional.of(GIF);

        if (length >= 12 && h[0] == 'R' && h[1] == 'I' && h[2] == 'F' && h[3] == 'F'
                && h[8] == 'W' && h[9] == 'E' && h[10] == 'B' && h[11] == 'P')
            return Optional.of(WEBP);

        return Optional.empty();
    }

    static Optional<MediaFormat> ofMimeType(String mimeType) {
        for (MediaFormat f : values())
            if (f.mimeType.equals(mimeType))
                return Optional.of(f);
        return Optional.empty();
    }

}
