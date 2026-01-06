package application;

import webserver.http.Cookie;
import webserver.http.HttpSession;
import webserver.handler.APIHandler;
import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import webserver.http.response.HttpResponseBuilder;
import webserver.http.response.HttpStatus;

public class LogoutHandler implements APIHandler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpSession session = request.getSession();
        if(session != null) {
            session.invalidate();
        }

        Cookie cookie = new Cookie( "");
        cookie.setMaxAge(0);

        return new HttpResponseBuilder().statusLine(HttpStatus.FOUND)
                .setCookie(cookie)
                .header(HttpHeader.LOCATION,  "/")
                .build();
    }
}
