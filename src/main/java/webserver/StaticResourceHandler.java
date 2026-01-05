package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;
import webserver.http.response.ResponseBody;

public class StaticResourceHandler implements ResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);

    private final ResourceProvider resourceProvider;

    public StaticResourceHandler(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        ResponseBody responseBody = resourceProvider.getResponseBody(path);
        return new HttpResponseBuilder().statusLine(HttpStatus.OK)
                .header(HttpHeader.CONTENT_TYPE, responseBody.getContentType().getMimeType())
                .header(HttpHeader.Content_LENGTH, String.valueOf(responseBody.getContentLength()))
                .body(responseBody.getContent())
                .build();
    }
}
