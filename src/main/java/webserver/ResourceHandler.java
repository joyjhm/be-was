package webserver;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface ResourceHandler {
    HttpResponse handle(HttpRequest httpRequest);
}
