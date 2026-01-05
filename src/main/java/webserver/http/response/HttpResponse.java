package webserver.http.response;

public class HttpResponse {

    HttpResponseStatusLine statusLine;
    HttpResponseHeader headers;
    byte[] body;

    HttpResponse(HttpResponseBuilder builder) {
        this.statusLine = builder.statusLine;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getResponseHead() {
        StringBuilder message = new StringBuilder();
        message.append(statusLine.getMessage());
        message.append(headers.getMessage());
        message.append("\r\n");
        return message.toString();
    }

    public byte[] getBody() {
        return body;
    }
}
