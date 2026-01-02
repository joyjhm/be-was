package webserver.response;

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

    public HttpResponseBuilder header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(this);
    }


}
