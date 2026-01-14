package webserver.convertor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.BadRequestException;
import webserver.http.HttpHeader;
import webserver.http.MultiPartFormData;
import webserver.http.request.HttpRequest;

public class MultipartFormDataConvertor implements HttpMessageConvertor<HttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(MultipartFormDataConvertor.class);
    private static final String BOUNDARY_PARAM = "boundary";
    private static final String MULTI_PART_FORM_DATA = "multipart/form-data";

    private static final String CRLF = "\r\n";
    private static final String BOUNDARY_MARKER = "--";

    private static final String NAME_PARAM = "name";
    private static final String FILENAME_PARAM = "filename";


    @Override
    public boolean canConvert(HttpRequest httpRequest) {
        String contentType = httpRequest.getHeader(HttpHeader.CONTENT_TYPE.getHeader());
        if (contentType == null) {
            return false;
        }

        return contentType.contains(MULTI_PART_FORM_DATA);
    }

    @Override
    public void convert(HttpRequest httpRequest) {

        String boundary = findBoundary(httpRequest);

        logger.debug("boundary: {}", boundary);

        List<byte[]> parts = parseBoundary(httpRequest, boundary);

        parseData(httpRequest, parts);
    }

    private String findBoundary(HttpRequest httpRequest) {
        String contentType = httpRequest.getHeader(HttpHeader.CONTENT_TYPE.getHeader());
        if (contentType == null) {
            throw new BadRequestException("Content-Type not found");
        }

        for (String part : contentType.split(";")) {
            String trimmed = part.trim();
            int eq = trimmed.indexOf("=");
            if (eq == -1) continue;

            String name = trimmed.substring(0, eq);
            if (BOUNDARY_PARAM.equalsIgnoreCase(name)) {
                return trimmed.substring(eq + 1);
            }
        }
        throw new BadRequestException("boundary not found");
    }

    private List<byte[]> parseBoundary(HttpRequest httpRequest, String boundary) {
        byte[] delimiter = (BOUNDARY_MARKER + boundary + CRLF).getBytes(StandardCharsets.US_ASCII);
        byte[] lastDelimiter = (BOUNDARY_MARKER + boundary + BOUNDARY_MARKER).getBytes(StandardCharsets.US_ASCII);

        byte[] body = httpRequest.getBody();

        List<byte[]> parts = new ArrayList<>();

        int pos = indexOfBytes(body, delimiter, 0);

        if (pos == -1) {
            throw new BadRequestException("boundary not found");
        }

        pos += delimiter.length;

        while (true) {
            int next = indexOfBytes(body, delimiter, pos);
            if (next == -1) {
                break;
            }

            parts.add(Arrays.copyOfRange(body, pos, next));
            pos = next + delimiter.length;
        }
        int end = indexOfBytes(body, lastDelimiter, pos);

        if(end == -1) {
            throw new BadRequestException("Invalid multipart/form-data body: final boundary not found");
        }

        parts.add(Arrays.copyOfRange(body, pos, end));

        return parts;
    }

    private void parseData(HttpRequest httpRequest, List<byte[]> parts) {
        parts.forEach(part -> {
            int index = indexOfBytes(part, (CRLF + CRLF).getBytes(StandardCharsets.US_ASCII), 0);
            byte[] headerBytes = Arrays.copyOfRange(part, 0, index);
            byte[] body = Arrays.copyOfRange(part, index + CRLF.length(), part.length);
            String headerText =
                    new String(headerBytes, StandardCharsets.UTF_8);

            String[] headerLine = headerText.split(CRLF);
            Map<String, String> headers = new HashMap<>();

            for (String s : headerLine) {
                String[] token = s.split(":");
                String key = token[0].trim().toLowerCase();
                String value = token[1].trim();

                headers.put(key.toLowerCase(), value);
            }

            MultiPartFormData multiPartFormData = createMultipartFormData(headers, body);
            httpRequest.setAttribute(multiPartFormData.name(), multiPartFormData);
        });
    }


    private MultiPartFormData createMultipartFormData(Map<String, String> headers, byte[] body) {
        String disposition = headers.get(HttpHeader.CONTENT_DISPOSITION.getHeader());

        if (disposition == null || disposition.isBlank()) {
            throw new BadRequestException("Malformed multipart part: Content-Disposition missing");
        }

        String name = extractQuotedParam(disposition, NAME_PARAM);
        String fileName = extractQuotedParam(disposition, FILENAME_PARAM);

        logger.debug("name: {}, fileName: {}", name, fileName);
        return new MultiPartFormData(name, fileName, headers, body);
    }

    private String extractQuotedParam(String disposition, String paramName) {
        String token = paramName + "=\"";
        int idx = disposition.indexOf(token);
        if (idx == -1) {
            return null;
        }

        int start = idx + token.length();
        int end = disposition.indexOf('"', start);
        if (end == -1) {
            return null;
        }

        return disposition.substring(start, end);
    }

    private int indexOfBytes(byte[] data, byte[] pattern, int from) {
        outer:
        for (int i = from; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
