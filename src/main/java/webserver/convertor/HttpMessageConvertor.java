package webserver.convertor;

import webserver.http.request.HttpRequest;

public interface HttpMessageConvertor<T> {

    boolean canConvert(T httpMessage);

    void convert(T httpMessage);
}
