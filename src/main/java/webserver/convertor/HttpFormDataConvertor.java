package webserver.convertor;

import webserver.http.HttpHeader;
import webserver.http.request.HttpRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class HttpFormDataConvertor implements HttpMessageConvertor<HttpRequest> {

    private static final String FORM_DATA_CONTENT_TYPE = "application/x-www-form-urlencoded";

    @Override
    public boolean canConvert(HttpRequest httpRequest) {
        String contentType = httpRequest.getHeader(HttpHeader.CONTENT_TYPE.getHeader());
        if (contentType == null) {
            return false;
        }

        return contentType.equals(FORM_DATA_CONTENT_TYPE);
    }

    @Override
    public void convert(HttpRequest httpRequest) {
        for (String pair : httpRequest.getBodyAsString().split("&")) {
            String[] kv = pair.split("=", 2);
            String key = kv[0];
            String value = kv.length > 1
                    ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                    : "";
            httpRequest.setAttribute(key, value);
        }
    }
}
