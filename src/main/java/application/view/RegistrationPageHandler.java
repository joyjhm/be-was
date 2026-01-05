package application.view;

import webserver.handler.ViewHandler;
import webserver.http.request.HttpRequest;

public class RegistrationPageHandler implements ViewHandler {

    private static final String REGISTRATION_RESOURCE_PATH = "/registration/index.html";

    @Override
    public String handle(HttpRequest request) {
        return REGISTRATION_RESOURCE_PATH;
    }
}
