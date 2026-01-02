package webserver.handler;

import webserver.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

public class APIHandlerRegistry {

    Map<String, APIHandler> handlerMap = new HashMap<>();

    public APIHandlerRegistry() {
        handlerMap.put("/user/create", new UserCreateHandler());
    }

    public APIHandler getHandlerMap(String path) {
        APIHandler handler = handlerMap.get(path);
        if(handler == null) {
            throw new NotFoundException("api not found");
        }
        return handler;
    }
}
