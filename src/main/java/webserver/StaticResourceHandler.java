package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseBuilder;

import java.io.*;

public class StaticResourceHandler implements ResourceHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);
    private final static String ROOT_PATH = System.getProperty("user.dir");
    private final static String DEFAULT_STATIC_RESOURCE_PATH = "/src/main/resources/static";

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        byte[] resource = getResource(httpRequest.getStartLine().getTarget());
        String contentType = getContentType(httpRequest.getStartLine().getTarget());
        return new HttpResponseBuilder().statusLine(200)
                .header("Content-Type", contentType)
                .header("Content-Length", String.valueOf(resource.length))
                .body(resource)
                .build();
    }

    public byte[] getResource(String path) {
        String filePath = ROOT_PATH + DEFAULT_STATIC_RESOURCE_PATH + path;

        try (FileInputStream fis = new FileInputStream(filePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;

            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("static resource read error: {}", e.getMessage());
        }

        return null;
    }

    public String getContentType(String path) {
        if(path.endsWith(".html")) {
            return "text/html;charset=utf-8";
        }
        else if(path.endsWith(".css")) {
            return "text/css;charset=utf-8";
        }
        else if(path.endsWith(".js")) {
            return "application/javascript;charset=utf-8";
        }
        else if(path.endsWith(".ico")) {
            return "image/x-icon";
        }
        else if(path.endsWith(".png")) {
            return "image/png";
        }
        else if(path.endsWith(".jpg")) {
            return "image/jpeg";
        }
        else if(path.endsWith(".svg")) {
            return "image/svg+xml";
        }
        return null;
    }
}
