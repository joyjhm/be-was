package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.convertor.ConvertorRegistry;
import webserver.convertor.HttpMessageConvertor;
import webserver.exception.NotFoundException;
import webserver.handler.*;
import webserver.http.HttpHeader;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;
import webserver.http.response.ResponseBody;


public class DynamicResourceHandler implements ResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(DynamicResourceHandler.class);

    private final APIHandlerRegistry apiHandlerRegistry;
    private final ViewHandlerRegistry viewHandlerRegistry;
    private final ResourceProvider resourceProvider;
    private final ConvertorRegistry convertorRegistry;

    public DynamicResourceHandler(
            APIHandlerRegistry apiHandlerRegistry,
            ViewHandlerRegistry viewHandlerRegistry,
            ResourceProvider resourceProvider,
            ConvertorRegistry convertorRegistry
    ) {
        this.apiHandlerRegistry = apiHandlerRegistry;
        this.viewHandlerRegistry = viewHandlerRegistry;
        this.resourceProvider = resourceProvider;
        this.convertorRegistry = convertorRegistry;
    }

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {

        String path = httpRequest.getPath();

        // TODO: Convertor 순회하며 가능한 타입은 변환 시키기, Request 뿐만 아니라 Response도 적용

        for (HttpMessageConvertor messageConvertor: convertorRegistry.getHttpMessageConvertors()) {
            if (messageConvertor.canConvert(httpRequest)) {
                messageConvertor.convert(httpRequest);
            }
        }

        APIHandler apiHandler = apiHandlerRegistry.getHandler(new RouteKey(httpRequest.getMethod(), path));
        if (apiHandler != null) {
            return apiHandler.handle(httpRequest);
        }

        ViewHandler viewHandler = viewHandlerRegistry.getHandler(new RouteKey(httpRequest.getMethod(), path));
        if (httpRequest.getMethod() == HttpMethod.GET && viewHandler != null) {
            String filepath = viewHandler.handle(httpRequest);
            ResponseBody body = resourceProvider.getResponseBody(filepath);
            return new HttpResponseBuilder().statusLine(HttpStatus.OK)
                    .header(HttpHeader.CONTENT_TYPE, body.getContentType().getMimeType())
                    .header(HttpHeader.Content_LENGTH, String.valueOf(body.getContentLength()))
                    .body(body.getContent())
                    .build();
        }
        logger.error("not found dynamic resource path: {}", path);
        throw new NotFoundException("not found dynamic resource");
    }

}
