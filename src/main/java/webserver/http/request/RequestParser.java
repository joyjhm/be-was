package webserver.http.request;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import webserver.http.HttpHeader;

public class RequestParser {
    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private static final String CRLF = "\r\n";
    private static final byte[] HEADER_TERMINATOR = "\r\n\r\n".getBytes();

    public HttpRequest parseHttpRequest(BufferedInputStream bin) throws IOException {
        String headerStr = readHttpHeader(bin);

        int idx = headerStr.indexOf(CRLF);
        String startLineStr = headerStr.substring(0, idx).trim();
        HttpRequestStartLine startLine = parseStartLine(startLineStr);

        String header = headerStr.substring(idx + CRLF.length()).trim();
        Map<String, String> headers = parseHeaders(header);
        byte[] body = parseBody(bin, headers);

        return new HttpRequest(startLine, headers, body);
    }

    private String readHttpHeader(BufferedInputStream bin) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int matched = 0;
        int read;

        while ((read = bin.read()) != -1) {
            byte b = (byte) read;
            buffer.write(b);

            if (b == HEADER_TERMINATOR[matched]) {
                matched++;
                if (matched == HEADER_TERMINATOR.length) {
                    break;
                }
            } else {
                matched = b == HEADER_TERMINATOR[0] ? 1 : 0;
            }
        }

        return buffer.toString(StandardCharsets.ISO_8859_1);
    }

    private HttpRequestStartLine parseStartLine(String startLine) {
        String[] splitStartLine = startLine.split(" ");

        logger.info("request method : {}, request path : {}", splitStartLine[0], splitStartLine[1]);

        return new HttpRequestStartLine(
                splitStartLine[0], parseURL(splitStartLine[1]), splitStartLine[2]
        );

    }

    private Map<String, String> parseHeaders(String headerStr) {
        Map<String, String> headers = new HashMap<>();

        String[] token = headerStr.split("\r\n");

        Arrays.stream(token).forEach(headerLine -> {
            int idx = headerLine.indexOf(':');

            if(idx == -1) {
                throw new BadRequestException("invalid header");
            }

            String key = headerLine.substring(0, idx).trim();
            String value = headerLine.substring(idx + 1).trim();
            headers.put(key.toLowerCase(), value);
        });

        return headers;
    }

    private byte[] parseBody(BufferedInputStream bin, Map<String,String> headers) throws IOException {
        String cl = headers.get(HttpHeader.Content_LENGTH.getHeader());

        if(cl == null) {
            return null;
        }

        int contentLength = Integer.parseInt(cl);
        byte[] body = new byte[contentLength];

        int read = 0;
        while (read < contentLength) {
            int r = bin.read(body, read, contentLength - read);
            if (r == -1) {
                throw new EOFException();
            }
            read += r;
        }
        return body;
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
