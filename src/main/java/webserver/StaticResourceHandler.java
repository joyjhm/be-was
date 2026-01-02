package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.InternalServerException;
import webserver.exception.NotFoundException;
import webserver.request.HttpRequest;
import webserver.response.HttpResponse;
import webserver.response.HttpResponseBuilder;
import webserver.response.HttpStatus;

import java.io.*;

public class StaticResourceHandler implements ResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(StaticResourceHandler.class);
    private final static String DEFAULT_STATIC_RESOURCE_PATH = "./src/main/resources/static";

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String path = httpRequest.getStartLine().getRequestURL().getPath();
        byte[] resource = getResource(path);
        String contentType = getContentType(path);
        return new HttpResponseBuilder().statusLine(HttpStatus.OK)
                .header("Content-Type", contentType)
                .header("Content-Length", String.valueOf(resource.length))
                .body(resource)
                .build();
    }

    public byte[] getResource(String path) {
        String filePath;
        if(path.equals("/")) {
            filePath =  DEFAULT_STATIC_RESOURCE_PATH + "/index.html";
        } else {
            filePath = DEFAULT_STATIC_RESOURCE_PATH + path;
        }

        try ( FileInputStream fis = new FileInputStream(filePath);
              ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;

            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
        catch (FileNotFoundException e) {
            throw new NotFoundException("File not found");
        }
        catch (IOException e) {
            logger.error("static resource read error: {}", e.getMessage());
            throw new InternalServerException("internal error");
        }


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
