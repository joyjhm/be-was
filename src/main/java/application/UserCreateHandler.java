package application;

import db.UserDatabase;
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

        String userId = (String) request.getAttribute("userId");
        String password = (String) request.getAttribute("password");
        String name = (String) request.getAttribute("name");
        String email = (String) request.getAttribute("email");

        validateFormData(userId, password, name, email);

        User newUser = User.of(
                userId, password, name, email
        );

        User findUser = userDatabase.findUserByUserId(userId).orElse(null);

        if (findUser != null && findUser.name().equals(name)) {
           throw  new BadRequestException("이미 아이디와 이름이 같은 회원이 존재합니다.");
        }

        logger.info("New User: {}", newUser);

        userDatabase.addUser(newUser);

        return new HttpResponseBuilder().
                statusLine(HttpStatus.FOUND).
                header(HttpHeader.LOCATION, "/").
                build();
    }

    private void validateFormData(String userId, String password, String name, String email) {
        if (userId.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
            throw new BadRequestException("필드의 빈 값이 들어갈 수 없습니다.");
        }

        if (userId.length() < 4 ||  password.length() < 4 || name.length() < 4 ||  email.length() < 4) {
            throw new BadRequestException("필드 길이는 4자 이상이어야 합니다");
        }
    }
}
