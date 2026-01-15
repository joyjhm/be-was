package webserver.handler;

import application.LoginHandler;
import application.LogoutHandler;
import application.PostHandler;
import application.UserCreateHandler;
import application.UserUpdateHandler;
import db.BoardDatabase;
import db.UserDatabase;
import webserver.http.request.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class APIHandlerRegistry {

    Map<RouteKey, APIHandler> handlerMap = new HashMap<>();

    public APIHandlerRegistry(UserDatabase userDatabase, BoardDatabase boardDatabase) {
        handlerMap.put(new RouteKey(HttpMethod.POST, "/user/create"), new UserCreateHandler(userDatabase));
        handlerMap.put(new RouteKey(HttpMethod.POST, "/login"), new LoginHandler(userDatabase));
        handlerMap.put(new RouteKey(HttpMethod.POST, "/logout"), new LogoutHandler());
        handlerMap.put(new RouteKey(HttpMethod.POST, "/post"), new PostHandler(boardDatabase));
        handlerMap.put(new RouteKey(HttpMethod.PATCH, "/user"), new UserUpdateHandler(userDatabase));
    }

    public APIHandler getHandler(RouteKey routeKey) {
        return handlerMap.get(routeKey);
    }

}
