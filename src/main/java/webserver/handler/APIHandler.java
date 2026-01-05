package webserver.handler;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface APIHandler {
    HttpResponse handle(HttpRequest request);
}
