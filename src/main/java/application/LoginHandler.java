package application;

import db.UserDatabase;
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

    private final UserDatabase userDatabase;

    public LoginHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        String userId = request.getAttribute("userId");

        User user = userDatabase.findUserByUserId(userId)
                .orElseThrow(() -> new AuthorizationException("User not found"));

        String password = request.getAttribute("password");
        if (!user.password().equals(password)) {
            throw new AuthorizationException("Wrong password");
        }

        HttpSession session = request.createSession();

        session.setAttribute("id", user.id());
        session.setAttribute("userId", user.userId());
        session.setAttribute("name", user.name());
        session.setAttribute("email", user.email());

        logger.info("session data: id={}, userId={}, name={}, email={}",
                session.getAttribute("id"),
                session.getAttribute("userId"),
                session.getAttribute("name"),
                session.getAttribute("email")
        );

        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .setCookie(new Cookie(session.getKey()))
                .header(HttpHeader.LOCATION,  "/")
                .build();
    }
}
