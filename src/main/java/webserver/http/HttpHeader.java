package webserver.http;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    Content_LENGTH("Content-Length"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie"),
    COOKIE("Cookie"),
    CONTENT_DISPOSITION("Content-Disposition");
    private String header;
    HttpHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header.toLowerCase();
    }
}
