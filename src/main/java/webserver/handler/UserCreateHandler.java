package webserver.handler;

import db.Database;
import model.User;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseBuilder;
import webserver.response.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Map;


public class UserCreateHandler implements APIHandler {

    public HttpResponse handle(HttpRequest request) {
        Map<String, String> params = request.getStartLine().getRequestURL().getParams();

        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        Database.addUser(user);

        //TODO: body 반환값 수정 필요
        return new HttpResponseBuilder().
                statusLine(HttpStatus.CREATED)
                .body("ok".getBytes(StandardCharsets.UTF_8)).build();
    }
}
