package webserver;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseBuilder;
import java.io.*;

public class HttpRequestDispatcher {

    private final StaticResourceHandler staticResourceHandler;
    private final ActionRoutingHandler actionRoutingHandler;

    public HttpRequestDispatcher(StaticResourceHandler staticResourceHandler, ActionRoutingHandler actionRoutingHandler) {
        this.staticResourceHandler = staticResourceHandler;
        this.actionRoutingHandler = actionRoutingHandler;
    }

    public HttpResponse dispatch(HttpRequest httpRequest) throws IOException {
        String path = httpRequest.getStartLine().getTarget();
        boolean isStatic = path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".svg");

        if(isStatic) {
            return staticResourceHandler.handle(httpRequest);

        } else {
//            return actionRoutingHandler.handle(httpRequest);
        }

        byte[] body = "<h1>Hello World</h1>".getBytes();

        return new HttpResponseBuilder().statusLine(200)
                .header("Content-Type", "text/html")
                .header("Conte  nt-Length", String.valueOf(body.length))
                .body(body)
                .build();
    }

}
