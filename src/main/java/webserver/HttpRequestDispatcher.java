package webserver;

import webserver.exception.HttpException;
import webserver.exception.HttpExceptionHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public class HttpRequestDispatcher {

    private final StaticResourceHandler staticResourceHandler;
    private final DynamicResourceHandler dynamicResourceHandler;
    private final HttpExceptionHandler httpExceptionHandler;

    public HttpRequestDispatcher(StaticResourceHandler staticResourceHandler, DynamicResourceHandler dynamicResourceHandler, HttpExceptionHandler httpExceptionHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.dynamicResourceHandler = dynamicResourceHandler;
        this.httpExceptionHandler = httpExceptionHandler;
    }

    public HttpResponse dispatch(HttpRequest httpRequest) {
        String path = httpRequest.getPath();
        boolean isStatic = path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".svg");

        try {
            if(isStatic) {
                return staticResourceHandler.handle(httpRequest);
            }
            return dynamicResourceHandler.handle(httpRequest);
        } catch (HttpException e) {
            return httpExceptionHandler.handle(httpRequest, e);
        }

    }

}
