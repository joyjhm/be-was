package webserver.filter;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;

public interface Filter {

    boolean shouldFilter(HttpRequest request);

    HttpResponse filter(HttpRequest httpRequest);
}
