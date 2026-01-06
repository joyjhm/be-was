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

    //TODO Cookie 헤더 확인 및 파싱 부분 분리 필요
    public HttpSession getSession() {
        if(headers.containsKey(HttpHeader.COOKIE.getHeader())) {
            String value = headers.get(HttpHeader.COOKIE.getHeader());
            String[] token = value.split(";");
            for (String s : token) {
                String[] pair = s.split("=");
                if(pair[0].equals("sid")) {
                    if(pair.length == 2) {
                        return SessionStore.getSession(pair[1]);
                    }
                }
            }
        }

        return SessionStore.getSession();
    }

}
