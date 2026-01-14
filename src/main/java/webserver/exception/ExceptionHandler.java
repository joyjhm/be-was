package webserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.ResourceProvider;
import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.ContentType;
import webserver.http.request.RequestParser;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

import java.nio.charset.StandardCharsets;
import webserver.http.response.ResponseBody;


public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private final ResourceProvider resourceProvider;

    private static final String EXCEPTION_PATH = "/exception";

    public ExceptionHandler(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    public HttpResponse httpExceptionHandle(HttpRequest request, HttpException e) {
        logger.error(e.getMessage(), e);

        int code = e.getHttpStatus().getCode();
        ResponseBody responseBody = resourceProvider.getResponseBody(EXCEPTION_PATH + "/" + code + ".html",
                new Model());

        return new HttpResponseBuilder().statusLine(e.getHttpStatus())
                .header(HttpHeader.CONTENT_TYPE, responseBody.getContentType().getMimeType())
                .header(HttpHeader.Content_LENGTH, String.valueOf(responseBody.getContentLength()))
                .body(responseBody.getContent())
                .build();

    }

    public HttpResponse globalExceptionHandle(HttpRequest request, Exception e) {
        logger.error(e.getMessage(), e);

        ResponseBody responseBody = resourceProvider.getResponseBody(
                EXCEPTION_PATH + "/" + HttpStatus.INTERNAL_SERVER_ERROR.getCode() + ".html", new Model());

        return new HttpResponseBuilder().statusLine(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeader.CONTENT_TYPE, responseBody.getContentType().getMimeType())
                .header(HttpHeader.Content_LENGTH, String.valueOf(responseBody.getContentLength()))
                .body(responseBody.getContent())
                .build();
    }
}
