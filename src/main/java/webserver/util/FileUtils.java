package webserver.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.InternalServerException;

public class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    static public byte[] readFile(String filePath) throws IOException {
        String decodedPath =
                java.net.URLDecoder.decode(filePath, StandardCharsets.UTF_8);

        logger.info("Trying to read resource from file: {}", decodedPath);

        try (FileInputStream fis = new FileInputStream(decodedPath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int len;

            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }

    static public void writeFile(String filePath, byte[] data) {
        try {
            Files.write(Path.of(filePath), data);
        } catch (IOException e) {
            throw new InternalServerException(e.getMessage());
        }

    }
}
