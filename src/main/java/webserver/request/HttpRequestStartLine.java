package webserver.request;

public class HttpRequestStartLine {

    private final HttpMethod httpMethod;
    private final RequestURL requestURL;
    private final String version;

    HttpRequestStartLine(String method, RequestURL requestURL, String version) {
        this.httpMethod = HttpMethod.valueOf(method);
        this.requestURL = requestURL;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public RequestURL getRequestURL() {
        return requestURL;
    }

    public String getVersion() {
        return version;
    }

}
