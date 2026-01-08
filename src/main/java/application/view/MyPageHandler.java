package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class MyPageHandler implements ViewHandler {

    private static final String MY_PAGE_RESOURCE_PATH = "/mypage/index.html";
    private static final String LOGIN_PAGE = "/login/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        if (request.getSession() == null || request.getSession().getAttribute("userId") == null) {
            return LOGIN_PAGE;
        }

        return MY_PAGE_RESOURCE_PATH;
    }
}
