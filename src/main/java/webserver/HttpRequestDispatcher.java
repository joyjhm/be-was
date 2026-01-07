package webserver;

import webserver.convertor.ConvertorRegistry;
import webserver.convertor.HttpMessageConvertor;
import webserver.exception.HttpException;
import webserver.exception.ExceptionHandler;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.resolver.HttpResolver;
import webserver.resolver.ResolverRegistry;

public class HttpRequestDispatcher {

    private final StaticResourceHandler staticResourceHandler;
    private final DynamicResourceHandler dynamicResourceHandler;
    private final ExceptionHandler exceptionHandler;
    private final ConvertorRegistry convertorRegistry;
    private final ResolverRegistry resolverRegistry;

    public HttpRequestDispatcher(
            StaticResourceHandler staticResourceHandler,
            DynamicResourceHandler dynamicResourceHandler,
            ExceptionHandler exceptionHandler,
            ResolverRegistry resolverRegistry,
            ConvertorRegistry convertorRegistry) {
        this.staticResourceHandler = staticResourceHandler;
        this.dynamicResourceHandler = dynamicResourceHandler;
        this.exceptionHandler = exceptionHandler;
        this.resolverRegistry = resolverRegistry;
        this.convertorRegistry = convertorRegistry;

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
            for (HttpResolver httpResolver: resolverRegistry.getHttpResolvers()) {
                httpResolver.resolve(httpRequest);
            }

            for (HttpMessageConvertor<HttpRequest> messageConvertor: convertorRegistry.getHttpMessageRequestConverters()) {
                if (messageConvertor.canConvert(httpRequest)) {
                    messageConvertor.convert(httpRequest);
                }
            }

            if(isStatic) {
                return staticResourceHandler.handle(httpRequest);
            }
            return dynamicResourceHandler.handle(httpRequest);
        } catch (HttpException e) {
            return exceptionHandler.httpExceptionHandle(httpRequest, e);
        } catch (Exception e) {
            return exceptionHandler.globalExceptionHandle(httpRequest, e);
        }

    }

}
