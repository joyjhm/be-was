package webserver.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            //TODO: Header Key Value 콜론 사이 공백 수정 - header-field = field-name ":" OWS field-value OWS
            String[] part = headerLine.split(": ");
            //TODO: Index 1번이 없을 가능성 고려 - 방어 로직 구현 필요
            headers.put(part[0], part[1]);
        }
        return headers;
    }

    private String parseBody(BufferedReader br, Map<String,String> headers) throws IOException {
        String cl = headers.get("Content-Length");

        if(cl == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append('\n');
        }
        return sb.toString();
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
