package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.exception.InternalServerException;
import webserver.exception.NotFoundException;
import webserver.http.response.ContentType;
import webserver.http.response.ResponseBody;
import webserver.util.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class ResourceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ResourceProvider.class);
    private final static String DEFAULT_STATIC_RESOURCE_PATH = "./src/main/resources/static";

    public ResponseBody getResponseBody(String path) {
        String filePath = DEFAULT_STATIC_RESOURCE_PATH + path;
        try {
            logger.info("Trying to read resource from file: {}", filePath);
            byte[] content = FileUtils.readFile(filePath);
            Optional<ContentType> contentType = ContentType.mimeTypeOfPath(filePath);
            if (contentType.isEmpty()) {
                throw new BadRequestException("Content type not found");
            }

            return new ResponseBody(content, contentType.get(), content.length);
        }
        catch (FileNotFoundException e) {
            throw new NotFoundException("File not found");
        }
        catch (IOException e) {
            logger.error("static resource read error: {}", e.getMessage());
            throw new InternalServerException("internal error");
        }
    }

}
