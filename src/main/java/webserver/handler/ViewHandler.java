package webserver.handler;

import webserver.http.request.HttpRequest;

public interface ViewHandler {

    String handle(HttpRequest request);
}
