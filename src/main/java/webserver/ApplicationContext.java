package webserver;

import db.BoardDatabase;
import db.BoardH2Database;
import db.ConnectionManager;
import db.H2ConnectionManager;
import db.JdbcTemplate;
import db.UserDatabase;
import db.UserH2Database;
import webserver.convertor.ConvertorRegistry;
import webserver.exception.ExceptionHandler;
import webserver.filter.FilterRegistry;
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


        ConnectionManager connectionManager = new H2ConnectionManager();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(connectionManager);

        UserDatabase userDatabase = new UserH2Database(jdbcTemplate);
        BoardDatabase boardDatabase = new BoardH2Database(jdbcTemplate);

        ViewHandlerRegistry viewHandlerRegistry = new ViewHandlerRegistry(userDatabase, boardDatabase);
        APIHandlerRegistry apiHandlerRegistry = new APIHandlerRegistry(userDatabase, boardDatabase);

        DynamicResourceHandler dynamicResourceHandler =
                new DynamicResourceHandler(
                        apiHandlerRegistry,
                        viewHandlerRegistry,
                        resourceProvider
                );

        ExceptionHandler exceptionHandler = new ExceptionHandler(resourceProvider);

        FilterRegistry filterRegistry = new FilterRegistry();
        ResolverRegistry resolverRegistry = new ResolverRegistry();
        ConvertorRegistry convertorRegistry = new ConvertorRegistry();

        this.requestParser = requestParser;
        this.dispatcher =
                new HttpRequestDispatcher(
                        staticResourceHandler,
                        dynamicResourceHandler,
                        exceptionHandler,
                        filterRegistry,
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
