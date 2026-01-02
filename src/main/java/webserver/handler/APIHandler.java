package webserver.handler;

import webserver.request.HttpRequest;
import webserver.response.HttpResponse;

public interface APIHandler {

    HttpResponse handle(HttpRequest request);
}
