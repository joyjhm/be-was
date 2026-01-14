package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.HttpRequest;
import webserver.http.request.RequestParser;
import webserver.http.response.HttpResponse;

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
            BufferedInputStream bin = new BufferedInputStream(in);

            HttpRequest httpRequest = requestParser.parseHttpRequest(bin);
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
            if(body != null) {
                dos.write(body, 0, body.length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
