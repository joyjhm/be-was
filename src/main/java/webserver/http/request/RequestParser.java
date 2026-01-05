package webserver.http.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    public HttpRequest parseHttpRequest(BufferedReader br) throws IOException {
        // startLine 파싱
        HttpRequestStartLine startLine = parseStartLine(br);
        Map<String,String> headers = parseHeaders(br);
        String body = parseBody(br, headers);

        return new HttpRequest(startLine, headers, body);
    }

    private HttpRequestStartLine parseStartLine(BufferedReader br) throws IOException {
        String startLine = br.readLine();
        String[] splitStartLine = startLine.split(" ");

        logger.info("request method : {}, request path : {}", splitStartLine[0], splitStartLine[1]);

        return new HttpRequestStartLine(
                splitStartLine[0], parseURL(splitStartLine[1]), splitStartLine[2]
        );

    }

    private Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            int idx = headerLine.indexOf(':');

            if(idx == -1) {
                throw new BadRequestException("invalid header");
            }

            String key = headerLine.substring(0, idx).strip();
            String value = headerLine.substring(idx + 1).strip();
            headers.put(key.toLowerCase(), value);
        }
        return headers;
    }

    private String parseBody(BufferedReader br, Map<String,String> headers) throws IOException {
        String cl = headers.get("content-length");

        if(cl == null) {
            return null;
        }

        int contentLength = Integer.parseInt(cl);
        char[] body = new char[contentLength];

        int read = 0;
        while (read < contentLength) {
            int r = br.read(body, read, contentLength - read);
            if (r == -1) {
                break;
            }
            read += r;
        }
        return new String(body);
    }

    private RequestURL parseURL(String url) {
        String[] tokens = url.split("\\?", 2);
        String path = tokens[0];

        Map<String, String> params = new HashMap<>();

        if (tokens.length > 1 && !tokens[1].isEmpty()) {
            String query = tokens[1];

            Arrays.stream(query.split("&"))
                    .forEach(pair -> {
                        String[] kv = pair.split("=", 2);
                        String key = kv[0];
                        String value = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8) : "";
                        params.put(key, value);
                    });
        }
        return new RequestURL(path, params);
    }


}
