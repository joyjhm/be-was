package webserver;

import webserver.convertor.ConvertorRegistry;
import webserver.exception.HttpExceptionHandler;
import webserver.handler.APIHandlerRegistry;
import webserver.handler.ViewHandlerRegistry;
import webserver.http.request.RequestParser;

public class ApplicationContext {

    private final HttpRequestDispatcher dispatcher;
    private final RequestParser requestParser;

    public ApplicationContext() {
        RequestParser requestParser = new RequestParser();
        ResourceProvider resourceProvider = new ResourceProvider();

        StaticResourceHandler staticResourceHandler =
                new StaticResourceHandler(resourceProvider);

        APIHandlerRegistry apiHandlerRegistry = new APIHandlerRegistry();
        ViewHandlerRegistry viewHandlerRegistry = new ViewHandlerRegistry();
        ConvertorRegistry convertorRegistry = new ConvertorRegistry();

        DynamicResourceHandler dynamicResourceHandler =
                new DynamicResourceHandler(
                        apiHandlerRegistry,
                        viewHandlerRegistry,
                        resourceProvider,
                        convertorRegistry
                );

        HttpExceptionHandler httpExceptionHandler = new HttpExceptionHandler();

        this.requestParser = requestParser;
        this.dispatcher =
                new HttpRequestDispatcher(
                        staticResourceHandler,
                        dynamicResourceHandler,
                        httpExceptionHandler
                );
    }

    public HttpRequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public RequestParser getRequestParser() {
        return requestParser;
    }
}
