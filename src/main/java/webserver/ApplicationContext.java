package webserver;

import db.ConnectionManager;
import db.H2ConnectionManager;
import db.UserH2Database;
import webserver.convertor.ConvertorRegistry;
import webserver.exception.ExceptionHandler;
import webserver.handler.APIHandlerRegistry;
import webserver.handler.ViewHandlerRegistry;
import webserver.http.request.RequestParser;
import webserver.resolver.ResolverRegistry;

public class ApplicationContext {

    private final HttpRequestDispatcher dispatcher;
    private final RequestParser requestParser;

    public ApplicationContext() {
        RequestParser requestParser = new RequestParser();
        ResourceProvider resourceProvider = new ResourceProvider();

        StaticResourceHandler staticResourceHandler =
                new StaticResourceHandler(resourceProvider);


        ViewHandlerRegistry viewHandlerRegistry = new ViewHandlerRegistry();

        ConnectionManager connectionManager = new H2ConnectionManager();

        APIHandlerRegistry apiHandlerRegistry = new APIHandlerRegistry(new UserH2Database(connectionManager));

        DynamicResourceHandler dynamicResourceHandler =
                new DynamicResourceHandler(
                        apiHandlerRegistry,
                        viewHandlerRegistry,
                        resourceProvider
                );

        ExceptionHandler exceptionHandler = new ExceptionHandler(resourceProvider);

        ResolverRegistry resolverRegistry = new ResolverRegistry();
        ConvertorRegistry convertorRegistry = new ConvertorRegistry();

        this.requestParser = requestParser;
        this.dispatcher =
                new HttpRequestDispatcher(
                        staticResourceHandler,
                        dynamicResourceHandler,
                        exceptionHandler,
                        resolverRegistry,
                        convertorRegistry
                );
    }

    public HttpRequestDispatcher getDispatcher() {
        return dispatcher;
    }

    public RequestParser getRequestParser() {
        return requestParser;
    }
}
