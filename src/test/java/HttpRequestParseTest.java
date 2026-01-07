import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.http.request.HttpMethod;
import webserver.http.request.HttpRequest;
import webserver.http.request.RequestParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;

public class HttpRequestParseTest {

    RequestParser requestParser = new RequestParser();

    @Test
    @DisplayName("정상 테스트")
    public void parseRequest() throws IOException {
        String rawRequest =
                "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "\r\n";

        InputStream in = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequest request = requestParser.parseHttpRequest(br);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("param query path 테스트")
    public void parseRequestWithParam() throws IOException {
        String rawRequest =
                "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "\r\n";

        InputStream in = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequest request = requestParser.parseHttpRequest(br);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getParam("userId")).isEqualTo("javajigi");
        assertThat(request.getParam("password")).isEqualTo("password");
        assertThat(request.getParam("name")).isEqualTo("박재성");
        assertThat(request.getParam("email")).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName("request body가 존재하는 경우")
    public void parseRequestWithBody() throws IOException {
        String rawRequest =
                "POST /user/create HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Content-length: 69\r\n" +
                "\r\n" +
                "userId=javajigi&password=password&name=박재성&email=javajigi@40slipp.net";

        InputStream in = new ByteArrayInputStream(rawRequest.getBytes(StandardCharsets.UTF_8));
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        HttpRequest request = requestParser.parseHttpRequest(br);

        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(request.getBody()).isEqualTo("userId=javajigi&password=password&name=박재성&email=javajigi@40slipp.net");
    }
}
