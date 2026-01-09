package webserver.exception;

import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.ContentType;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

import java.nio.charset.StandardCharsets;


public class ExceptionHandler {

    public HttpResponse httpExceptionHandle(HttpRequest request, HttpException e) {
        String json = String.format(
                """
                {
                  "path": "%s",
                  "message": "%s"
                }
                """,
                request.getPath(),
                e.getMessage()
        );
        byte[] body = json.getBytes(StandardCharsets.UTF_8);

        return new HttpResponseBuilder().statusLine(e.getHttpStatus())
                .header(HttpHeader.CONTENT_TYPE, ContentType.JSON.getMimeType())
                .header(HttpHeader.Content_LENGTH, String.valueOf(body.length))
                .body(json)
                .build();
    }

    public HttpResponse globalExceptionHandle(HttpRequest request, Exception e) {
        String json = String.format(
                """
                {
                  "path": "%s",
                  "message": "%s"
                }
                """,
                request.getPath(),
                e.getMessage()
        );
        byte[] body = json.getBytes(StandardCharsets.UTF_8);

        return new HttpResponseBuilder().statusLine(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeader.CONTENT_TYPE, ContentType.JSON.getMimeType())
                .header(HttpHeader.Content_LENGTH, String.valueOf(body.length))
                .body(json)
                .build();
    }
}
