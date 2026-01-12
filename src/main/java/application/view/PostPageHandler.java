package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class PostPageHandler implements ViewHandler {

    private static final String POST_PAGE_PATH = "/article/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        return POST_PAGE_PATH;
    }
}
