package webserver;

import webserver.request.StaticResourceHandler;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseBuilder;
import java.io.*;

public class Router {

    private final StaticResourceHandler staticResourceHandler;

    public Router(StaticResourceHandler staticResourceHandler) {
        this.staticResourceHandler = staticResourceHandler;
    }

    public HttpResponse route(String path) throws IOException {

        boolean isStatic = path.endsWith(".html")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".svg");

        if(isStatic) {
            byte[] resource = staticResourceHandler.getResource(path);
            String contentType = staticResourceHandler.getContentType(path);
            return new HttpResponseBuilder().statusLine(200)
                    .header("Content-Type", contentType)
                    .header("Content-Length", String.valueOf(resource.length))
                    .body(resource)
                    .build();
        }

        byte[] body = "<h1>Hello World</h1>".getBytes();

        return new HttpResponseBuilder().statusLine(200)
                .header("Content-Type", "text/html")
                .header("Conte  nt-Length", String.valueOf(body.length))
                .body(body)
                .build();
    }

}
