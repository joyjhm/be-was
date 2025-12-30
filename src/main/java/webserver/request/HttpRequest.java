package webserver.request;

import java.util.Map;

public class HttpRequest {

    private final HttpRequestStartLine startLine;
    private final Map<String, String> headers;
    private final String body;

    HttpRequest(HttpRequestStartLine startLine, Map<String, String> headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }


    public HttpRequestStartLine getStartLine() {
        return startLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }


}
