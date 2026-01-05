package application;

import db.Database;
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


    public HttpResponse handle(HttpRequest request) {

        User newUser = new User(
                request.getAttribute("userId"),
                request.getAttribute("password"),
                request.getAttribute("name"),
                request.getAttribute("email")
        );

        logger.info("New User: {}", newUser);

        if (newUser.getUserId() == null) {
            throw new BadRequestException("User ID is required");
        }

        Database.addUser(newUser);

        return new HttpResponseBuilder().
                statusLine(HttpStatus.FOUND).
                header(HttpHeader.LOCATION,  "/index.html").
                build();
    }
}
