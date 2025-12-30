package webserver.response;

public class HttpResponseStatusLine {
    String version;
    int status;
    String reason;

    public HttpResponseStatusLine(String version, int status, String reason) {
        this.version = version;
        this.status = status;
        this.reason = reason;
    }

    public String getMessage() {
        return String.format("%s %d %s\r\n", version, status, reason);
    }
}
