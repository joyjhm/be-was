package webserver.handler;

import application.view.LoginPageHandler;
import application.view.MainPageHandler;
import application.view.MyPageHandler;
import application.view.PostPageHandler;
import application.view.RegistrationPageHandler;
import webserver.http.request.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class ViewHandlerRegistry {
    Map<RouteKey, ViewHandler> handlerMap = new HashMap<>();

    public ViewHandlerRegistry() {
        handlerMap.put(new RouteKey(HttpMethod.GET, "/"), new MainPageHandler());
        handlerMap.put(new RouteKey(HttpMethod.GET,"/registration"), new RegistrationPageHandler());
        handlerMap.put(new RouteKey(HttpMethod.GET,"/login"), new LoginPageHandler());
        handlerMap.put(new RouteKey(HttpMethod.GET,"/mypage"), new MyPageHandler());
        handlerMap.put(new RouteKey(HttpMethod.GET,"/article"), new PostPageHandler());
    }

    public ViewHandler getHandler(RouteKey routeKey) {
        return handlerMap.get(routeKey);
    }
}
