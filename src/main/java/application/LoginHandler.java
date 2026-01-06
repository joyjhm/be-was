package application;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Cookie;
import webserver.http.HttpSession;
import webserver.exception.AuthorizationException;
import webserver.handler.APIHandler;
import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

public class LoginHandler implements APIHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);

    @Override
    public HttpResponse handle(HttpRequest request) {
        String userId = request.getAttribute("userId");
        User user = Database.findUserById(userId);

        if(user == null) {
            throw new AuthorizationException("User not found");
        }

        String password = request.getAttribute("password");
        if (!user.getPassword().equals(password)) {
            throw new AuthorizationException("Wrong password");
        }

        HttpSession session = request.getSession();

        session.setAttribute("userId", user.getUserId());
        session.setAttribute("name", user.getName());
        session.setAttribute("email", user.getEmail());

        logger.info("sessionId: {}", session.getSid());

        logger.info("session data: userId={}, name={}, email={}",
                session.getAttribute("userId"),
                session.getAttribute("name"),
                session.getAttribute("email")
        );

        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .setCookie(new Cookie(session.getSid()))
                .header(HttpHeader.LOCATION,  "/")
                .build();

    }
}
