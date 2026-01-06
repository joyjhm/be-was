package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class MyPageHandler implements ViewHandler {

    private static final String MY_PAGE_RESOURCE_PATH = "/mypage/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        return MY_PAGE_RESOURCE_PATH;
    }
}
