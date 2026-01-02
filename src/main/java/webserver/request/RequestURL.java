package webserver.request;

import java.util.Map;

public class RequestURL {
    String path;
    Map<String, String> params;

    public RequestURL(String path, Map<String, String> params) {
        this.path = path;
        this.params = params;
    }

    public String getPath() {
        return path;
    }
    public Map<String, String> getParams() {
        return params;
    }
}
