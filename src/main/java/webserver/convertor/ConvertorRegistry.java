package webserver.convertor;

import webserver.http.request.HttpRequest;

import java.util.LinkedList;
import java.util.List;

public class ConvertorRegistry {

    List<HttpMessageConvertor<HttpRequest>> httpMessageRequestConverters = new LinkedList<>();

    public ConvertorRegistry() {
        httpMessageRequestConverters.add(new HttpFormDataConvertor());
    }

    public List<HttpMessageConvertor<HttpRequest>> getHttpMessageRequestConverters() {
        return httpMessageRequestConverters;
    }
}
