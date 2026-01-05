package application.view;

import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class MainPageHandler implements ViewHandler {

    private static final String MAIN_PAGE_RESOURCE_PATH = "/index.html";

    @Override
    public String handle(HttpRequest request) {
        return MAIN_PAGE_RESOURCE_PATH;
    }
}
