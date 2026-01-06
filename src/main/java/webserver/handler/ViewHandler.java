package webserver.handler;

import webserver.Model;
import webserver.http.request.HttpRequest;

public interface ViewHandler {

    String handle(HttpRequest request, Model model);
}
