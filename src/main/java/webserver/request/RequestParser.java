package webserver.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

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

        return new HttpRequestStartLine(
                splitStartLine[0], splitStartLine[1], splitStartLine[2]
        );

    }

    private Map<String, String> parseHeaders(BufferedReader br) throws IOException {
        String headerLine;
        Map<String, String> headers = new HashMap<>();
        while ((headerLine = br.readLine()) != null && !headerLine.isEmpty()) {
            String[] part = headerLine.split(": ");
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

}
