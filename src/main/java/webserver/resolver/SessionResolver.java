package webserver.resolver;

import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;

public class SessionResolver implements HttpResolver {

    private static final String SESSION_KEY = "sid";

    @Override
    public void resolve(HttpRequest request) {
        String value = request.getHeader(HttpHeader.COOKIE.getHeader());

        if(value != null) {
            String[] token = value.split(";");
            for (String s : token) {
                String[] pair = s.trim().split("=");
                if(pair[0].trim().equals(SESSION_KEY) && pair.length == 2) {
                    request.setSession(pair[1].trim());
                }
            }
        }
    };

}