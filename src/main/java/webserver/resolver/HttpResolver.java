package webserver.resolver;

import webserver.http.request.HttpRequest;

public interface HttpResolver {

    void resolve(HttpRequest request);
}
