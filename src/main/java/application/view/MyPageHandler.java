package application.view;

import db.UserDatabase;
import model.User;
import webserver.Model;
import webserver.exception.InternalServerException;
import webserver.handler.ViewHandler;
import webserver.http.HttpSession;
import webserver.http.request.HttpRequest;

public class MyPageHandler implements ViewHandler {

    private static final String MY_PAGE_RESOURCE_PATH = "/mypage/index.html";

    private final UserDatabase userDatabase;

    public MyPageHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public String handle(HttpRequest request, Model model) {
        HttpSession session = request.getSession();
        Long id = (Long) session.getAttribute("id");

        User user = userDatabase.findUserById(id).orElseThrow(() ->
                new InternalServerException(""));

        System.out.println("image Path: " + user.imagePath());

        model.setAttribute("profileImage", user.imagePath());

        model.setAttribute("name", user.name());

        return MY_PAGE_RESOURCE_PATH;
    }
}
