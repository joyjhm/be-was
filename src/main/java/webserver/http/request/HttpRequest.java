package webserver.http.request;

import db.SessionStore;
import webserver.http.HttpSession;
import webserver.http.HttpHeader;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpRequestStartLine startLine;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, String> attributes = new HashMap<>();
    private HttpSession session;


    HttpRequest(HttpRequestStartLine startLine, Map<String, String> headers, String body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public String getPath() {
        return startLine.getPath();
    }

    public String getParam(String parameterName) {
        return startLine.getParam(parameterName);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public HttpSession getSession() {
        return session;
    }

    public HttpSession createSession() {
        return SessionStore.getSession();
    }

    public void setSession(String key) {
        session = SessionStore.getSession(key);
    }
}
