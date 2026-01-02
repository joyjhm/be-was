package webserver;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import java.io.*;

public class HttpRequestDispatcher {

    private final StaticResourceHandler staticResourceHandler;
    private final DynamicResourceHandler dynamicResourceHandler;

    public HttpRequestDispatcher(StaticResourceHandler staticResourceHandler, DynamicResourceHandler dynamicResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.dynamicResourceHandler = dynamicResourceHandler;
    }

    public HttpResponse dispatch(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getStartLine().getRequestURL().getPath();
        boolean isStatic = path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".svg")
                || path.equals("/");

        if(isStatic) {
            return staticResourceHandler.handle(httpRequest);
        }

        return dynamicResourceHandler.handle(httpRequest);

    }

}
