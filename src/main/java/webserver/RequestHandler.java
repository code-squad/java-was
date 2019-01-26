package webserver;

import java.io.*;
import java.net.Socket;

import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        // inputstream 요청, outputstream 응답
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = RequestGenerator.generateRequest(br);
            HttpResponse response = ResponseGenerator.generateResponse(dos);

            Dispatcher.dispatch(request, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
