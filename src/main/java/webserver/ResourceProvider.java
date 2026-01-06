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
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class ResourceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ResourceProvider.class);
    private final static String DEFAULT_STATIC_RESOURCE_PATH = "./src/main/resources/static";

    public ResponseBody getResponseBody(String path, Model model) {
        String filePath = DEFAULT_STATIC_RESOURCE_PATH + path;
        try {
            logger.info("Trying to read resource from file: {}", filePath);

            byte[] content = FileUtils.readFile(filePath);
            Optional<ContentType> contentType = ContentType.mimeTypeOfPath(filePath);
            if (contentType.isEmpty()) {
                throw new BadRequestException("Content type not found");
            }

            if(contentType.get().equals(ContentType.HTML)) {
                String stringBody = new String(content, StandardCharsets.UTF_8);

                for (Map.Entry<String, String> entry : model.getAttributes().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    if(stringBody.contains(String.format("{{%s}}", key))){
                        stringBody = stringBody.replace(String.format("{{%s}}", key), value);
                    }
                }
                byte[] body = stringBody.getBytes(StandardCharsets.UTF_8);
                return new ResponseBody(body, ContentType.HTML, body.length);
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
