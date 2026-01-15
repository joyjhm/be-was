package webserver.http;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum ContentType {

    HTML(List.of("html"), "text/html;charset=utf-8"),
    CSS(List.of("css"), "text/css;charset=utf-8"),
    JS(List.of("js"), "application/javascript;charset=utf-8"),
    ICO(List.of("ico"), "image/x-icon"),
    PNG(List.of("png"), "image/png"),
    JPG(List.of("jpg", "jpeg"), "image/jpeg"),
    SVG(List.of("svg"), "image/svg+xml"),
    JSON(List.of("json"), "application/json;charset=utf-8");


    private final List<String> extensions;
    private final String mimeType;


    ContentType(List<String> extensions, String mimeType) {
        this.extensions = extensions;
        this.mimeType = mimeType;
    }

    public static Optional<ContentType> mimeTypeOfPath(String path) {
        int idx = path.lastIndexOf('.');
        if (idx == -1) {
            return Optional.empty();
        }
        return mimeTypeFromExtension(path.substring(idx + 1));
    }

    private static Optional<ContentType> mimeTypeFromExtension(String ext) {
        String normalized = ext.toLowerCase();
        for (ContentType t : values()) {
            if (t.extensions.contains(normalized)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public static boolean isStaticResource(String path) {
        ContentType contentType = ContentType.mimeTypeOfPath(path).orElse(null);
        return contentType == HTML ||
                contentType == CSS || contentType == JS || contentType == ICO || contentType == PNG
                || contentType == JPG || contentType == SVG;
    }

    public String getMimeType() {
        return mimeType;
    }


}
