package webserver.request;

public class HttpRequestStartLine {

    private final HttpMethod httpMethod;
    private final String target;
    private final String version;

    HttpRequestStartLine(String method, String target, String version) {
        this.httpMethod = HttpMethod.valueOf(method);
        this.target = target;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getTarget() {
        return target;
    }

    public String getVersion() {
        return version;
    }

}
