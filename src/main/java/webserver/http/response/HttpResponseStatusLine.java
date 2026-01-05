package webserver.http.response;

public class HttpResponseStatusLine {
    String version;
    int code;
    HttpStatus reason;

    public HttpResponseStatusLine(String version, int status, HttpStatus reason) {
        this.version = version;
        this.code = status;
        this.reason = reason;
    }

    public String getMessage() {
        return String.format("%s %d %s\r\n", version, code, reason);
    }
}
