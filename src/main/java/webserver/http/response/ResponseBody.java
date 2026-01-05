package webserver.http.response;

public class ResponseBody {

    byte[] content;
    ContentType contentType;
    int contentLength;

    public ResponseBody(byte[] content, ContentType contentType, int contentLength) {
        this.content = content;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }
}
