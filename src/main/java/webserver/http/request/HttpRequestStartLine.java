package webserver.http.request;

public class HttpRequestStartLine {

    private final HttpMethod httpMethod;
    private final RequestURL requestURL;
    private final String version;

    public HttpRequestStartLine(String method, RequestURL requestURL, String version) {
        this.httpMethod = HttpMethod.valueOf(method);
        this.requestURL = requestURL;
        this.version = version;
    }

    public HttpMethod getMethod() {
        return httpMethod;
    }

    public String getParam(String parameterName) {
        return requestURL.getParam(parameterName);
    }

    public String getPath() {
        return requestURL.getPath();
    }

    public String getVersion() {
        return version;
    }

}
