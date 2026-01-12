package application;

import db.UserDatabase;
import db.UserMemoryDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.handler.APIHandler;
import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

public class UserCreateHandler implements APIHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserCreateHandler.class);

    private final UserDatabase userDatabase;

    public UserCreateHandler(UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }

    public HttpResponse handle(HttpRequest request) {

        String userId = request.getAttribute("userId");
        String password = request.getAttribute("password");
        String name = request.getAttribute("name");
        String email = request.getAttribute("email");

        if (userId.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
            throw new BadRequestException("Required fields are missing.");
        }

        User newUser = new User(
                userId, password, name, email
        );

        logger.info("New User: {}", newUser);

        userDatabase.addUser(newUser);

        return new HttpResponseBuilder().
                statusLine(HttpStatus.FOUND).
                header(HttpHeader.LOCATION, "/").
                build();
    }
}
