package webserver.http.request;

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

    public String getParam(String parameterName) {
        return params.get(parameterName);
    }

}
