package webserver.filter;

import java.util.HashSet;
import java.util.Set;
import webserver.Model;
import webserver.ResourceProvider;
import webserver.handler.RouteKey;
import webserver.http.HttpHeader;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;
import webserver.http.response.ResponseBody;
import webserver.util.FileUtils;

public class AuthFilter implements Filter {

    private static final Set<RouteKey> excludeUrls = new HashSet<>();
    private static final String MAIN_PATH = "/";

    public AuthFilter() {
        excludeUrls.add(new RouteKey(HttpMethod.GET, "/"));
        excludeUrls.add(new RouteKey(HttpMethod.GET, "/login"));
        excludeUrls.add(new RouteKey(HttpMethod.POST, "/login"));
        excludeUrls.add(new RouteKey(HttpMethod.POST, "/logout"));
        excludeUrls.add(new RouteKey(HttpMethod.POST, "/user/create"));
        excludeUrls.add(new RouteKey(HttpMethod.GET, "/registration"));
    }

    public boolean shouldFilter(HttpRequest request) {

        if (!excludeUrls.contains(new RouteKey(request.getMethod(), request.getPath()))) {
            return request.getSession() == null || request.getSession().getAttribute("userId") == null;
        }

        return false;
    }


    public HttpResponse filter(HttpRequest request) {
        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .header(HttpHeader.LOCATION, MAIN_PATH).build();
    }

}
