package webserver;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface ResourceHandler {
    HttpResponse handle(HttpRequest httpRequest);
}
