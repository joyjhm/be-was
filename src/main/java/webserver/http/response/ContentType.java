package webserver.http.response;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum ContentType {

    HTML(Collections.singletonList("html"), "text/html;charset=utf-8"),
    CSS(Collections.singletonList("css"),"text/css;charset=utf-8"),
    JS(Collections.singletonList("js"), "application/javascript;charset=utf-8"),
    ICO(Collections.singletonList("ico"), "image/x-icon"),
    PNG(Collections.singletonList("png"), "image/png"),
    JPG(Collections.singletonList("jpg, jpeg"), "image/jpeg"),
    SVG(Collections.singletonList("svg"), "image/svg+xml"),
    JSON(Collections.singletonList("json"), "application/json;charset=utf-8");
    

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

        for (ContentType t : values()) {
            if (t.extensions.contains(ext)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public String getMimeType() {
        return mimeType;
    }


}
