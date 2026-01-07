package application.view;

import webserver.Model;
import webserver.handler.ViewHandler;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;

public class MainPageHandler implements ViewHandler {

    private static final String MAIN_PAGE_PATH = "/index.html";
    private static final String LOGGED_IN_MAIN_PAGE_PATH = "/main/index.html";

    @Override
    public String handle(HttpRequest request, Model model) {
        HttpSession session = request.getSession();

        if (session == null) {
            return MAIN_PAGE_PATH;
        }

        String name = (String) session.getAttribute("name");
        if (name == null) {
            return MAIN_PAGE_PATH;
        }

        model.setAttribute("name", name);
        return LOGGED_IN_MAIN_PAGE_PATH;
    }
}
