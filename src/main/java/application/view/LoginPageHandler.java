package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class LoginPageHandler implements ViewHandler {

    private static final String LOGIN_RESOURCE_PATH = "/login/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        return LOGIN_RESOURCE_PATH;
    }
}
