package webserver.http;


import java.util.Map;

public record MultiPartFormData(
        String name,
        String fileName,
        Map<String, String>headers,
        byte[] body
)  {

    public String getBodyAsString() {
        return new String(body);
    }
}
