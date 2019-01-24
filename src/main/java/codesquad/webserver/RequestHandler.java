package codesquad.webserver;

import codesquad.model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = new HttpRequest(in);
            log.debug("httpRequest : {}", httpRequest);

            if(MappingHandler.hasMappingPath(httpRequest)) {
                MappingHandler.invoke(httpRequest);
            }

            log.debug(httpRequest.toString());
            ViewHandler.resolve(out, httpRequest.toResponse());

        } catch (Exception e) {
            log.error("Error Message :", e);
        }
    }
}
