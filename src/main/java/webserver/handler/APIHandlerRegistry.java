package webserver.handler;

import application.LoginHandler;
import application.LogoutHandler;
import application.UserCreateHandler;
import db.UserDatabase;
import webserver.http.request.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class APIHandlerRegistry {

    Map<RouteKey, APIHandler> handlerMap = new HashMap<>();

    public APIHandlerRegistry(UserDatabase userDatabase) {
        handlerMap.put(new RouteKey(HttpMethod.POST, "/user/create"), new UserCreateHandler(userDatabase));
        handlerMap.put(new RouteKey(HttpMethod.POST, "/login"), new LoginHandler(userDatabase));
        handlerMap.put(new RouteKey(HttpMethod.POST, "/logout"), new LogoutHandler());
    }

    public APIHandler getHandler(RouteKey routeKey) {
        return handlerMap.get(routeKey);
    }

}
