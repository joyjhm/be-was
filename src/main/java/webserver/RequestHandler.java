package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.RequestParser;
import webserver.response.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final RequestParser requestParser;
    private final HttpRequestDispatcher httpRequestDispatcher;

    public RequestHandler(Socket connectionSocket, RequestParser requestParser, HttpRequestDispatcher httpRequestDispatcher) {
        this.connection = connectionSocket;
        this.requestParser = requestParser;
        this.httpRequestDispatcher = httpRequestDispatcher;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            InputStreamReader isr  = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            HttpRequest httpRequest = requestParser.parseHttpRequest(br);
            HttpResponse response = httpRequestDispatcher.dispatch(httpRequest);

            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(response.getResponseHead());
            byte[] body = response.getBody();
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
