package webserver.convertor;

import webserver.http.request.HttpRequest;

public interface HttpMessageConvertor {

    boolean canConvert(HttpRequest httpRequest);

    void convert(HttpRequest httpRequest);
}
