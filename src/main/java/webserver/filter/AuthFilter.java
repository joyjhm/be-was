package webserver.filter;

import java.util.HashSet;
import java.util.Set;
import webserver.handler.RouteKey;
import webserver.http.HttpHeader;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

public class AuthFilter implements Filter {

    private static final Set<RouteKey> protectedUrls = new HashSet<>();
    private static final String LOGIN_PAGE_PATH = "login";

    public AuthFilter() {
        protectedUrls.add(new RouteKey(HttpMethod.POST, "/post"));
        protectedUrls.add(new RouteKey(HttpMethod.PATCH, "/user"));
        protectedUrls.add(new RouteKey(HttpMethod.POST, "/logout"));
        protectedUrls.add(new RouteKey(HttpMethod.GET, "/mypage"));
        protectedUrls.add(new RouteKey(HttpMethod.GET, "/article"));
    }

    public boolean shouldFilter(HttpRequest request) {
        RouteKey key = new RouteKey(request.getMethod(), request.getPath());

        if (!protectedUrls.contains(key)) {
            return false;
        }

        return request.getSession() == null
                || request.getSession().getAttribute("userId") == null;
    }


    public HttpResponse filter(HttpRequest request) {
        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, LOGIN_PAGE_PATH).build();
    }

}
