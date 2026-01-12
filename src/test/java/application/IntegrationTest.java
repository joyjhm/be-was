package application;

import db.UserDatabase;
import db.UserMemoryDatabase;
import model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.WebServer;
import webserver.http.response.HttpStatus;
import webserver.util.FileUtils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.*;

public class IntegrationTest {

    @BeforeAll
    public static void beforeAll() {
        String[] args = new String[1];
        args[0] = "8080";
        new Thread(() -> {
            try {
                WebServer.main(args);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    UserDatabase userDatabase = new UserMemoryDatabase();


    @Test
    @DisplayName("메인 페이지")
    public void mainPageTest() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(new URI("http://localhost:8080"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(response.uri()).isEqualTo(new URI("http://localhost:8080"));
        assertThat(response.body().getBytes()).isEqualTo(FileUtils.readFile("src/main/resources/static/index.html"));
    }

    @Test
    @DisplayName("유저 생성")
    public void userCreateTest() throws Exception {
        String body = "userId=test&password=1234&name=test&email=test@example.com";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI("http://localhost:8080/user/create"))
                .version(HttpClient.Version.HTTP_1_1)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FOUND.getCode());
        assertThat(response.headers().firstValue("Location").get()).isEqualTo("/");
        assertThat(response.uri()).isEqualTo(new URI("http://localhost:8080/user/create"));
        assertThat(userDatabase.findUserById("test")).isNotNull();
    }

    @Test
    @DisplayName("로그인")
    public void loginTest() throws Exception {
        User user = new User("test", "1234", "test", "test@example.com");
        userDatabase.addUser(user);

        String body = "userId=test&password=1234";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .uri(new URI("http://localhost:8080/login"))
                .version(HttpClient.Version.HTTP_1_1)
                .setHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FOUND.getCode());
        assertThat(response.headers().firstValue("Location").get()).isEqualTo("/");
        assertThat(response.uri()).isEqualTo(new URI("http://localhost:8080/login"));
    }

    @Test
    @DisplayName("로그아웃")
    public void logoutTest() throws Exception {
        User user = new User("test", "1234", "test", "test@example.com");
        userDatabase.addUser(user);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .uri(new URI("http://localhost:8080/logout"))
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FOUND.getCode());
        assertThat(response.headers().firstValue("Location").get()).isEqualTo("/");
        assertThat(response.uri()).isEqualTo(new URI("http://localhost:8080/logout"));
    }
}
