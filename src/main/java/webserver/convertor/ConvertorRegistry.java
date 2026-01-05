package webserver.convertor;

import java.util.LinkedList;
import java.util.List;

public class ConvertorRegistry {

    List<HttpMessageConvertor> httpMessageConvertors = new LinkedList<HttpMessageConvertor>();

    public ConvertorRegistry() {
        httpMessageConvertors.add(new HttpFormDataConvertor());
    }

    public List<HttpMessageConvertor> getHttpMessageConvertors() {
        return httpMessageConvertors;
    }
}
