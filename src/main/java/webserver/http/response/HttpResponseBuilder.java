package webserver.http.response;

import webserver.http.HttpHeader;

import java.nio.charset.StandardCharsets;

public class HttpResponseBuilder {

    HttpResponseStatusLine statusLine;
    HttpResponseHeader headers = new HttpResponseHeader();
    byte[] body;

    public HttpResponseBuilder statusLine(HttpStatus status) {
        return statusLine("HTTP/1.1", status);
    }

    public HttpResponseBuilder statusLine(String version, HttpStatus status) {
        this.statusLine = new HttpResponseStatusLine(version, status.getCode(), status);
        return this;
    }

    public HttpResponseBuilder header(HttpHeader name, String value) {
        this.headers.put(name.getHeader(), value);
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponseBuilder body(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(this);
    }


}
