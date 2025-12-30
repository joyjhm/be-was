package webserver.response;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseHeader {
    Map<String, String> headers = new HashMap<>();

    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            sb.append(String.format("%s: %s\r\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    public void put(String key, String value) {
        this.headers.put(key, value);
    }
}
