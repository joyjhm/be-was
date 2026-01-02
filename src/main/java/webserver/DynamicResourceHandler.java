package webserver;

import webserver.handler.APIHandler;
import webserver.handler.APIHandlerRegistry;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;


public class DynamicResourceHandler implements ResourceHandler {

    private final APIHandlerRegistry apiHandlerRegistry;

    public DynamicResourceHandler(APIHandlerRegistry apiHandlerRegistry) {
        this.apiHandlerRegistry = apiHandlerRegistry;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {

        String path = httpRequest.getStartLine().getRequestURL().getPath();
        APIHandler apiHandler = apiHandlerRegistry.getHandlerMap(path);
        return apiHandler.handle(httpRequest);
    }

}
