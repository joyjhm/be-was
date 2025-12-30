package webserver.response;

public class HttpResponseBuilder {


    HttpResponseStatusLine statusLine;
    HttpResponseHeader headers = new HttpResponseHeader();
    byte[] body;

    public HttpResponseBuilder statusLine(int status) {
        return statusLine("HTTP/1.1", status, "OK");
    }

    public HttpResponseBuilder statusLine(String version, int status, String reason) {
        this.statusLine = new HttpResponseStatusLine(version, status, reason);
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
