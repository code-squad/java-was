package webserver;

import controllers.RequestMapper;
import controllers.RequestProcessor;
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

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);

            RequestProcessor processor = RequestMapper.getProcessor(request);

            HttpResponse response = processor.processRequest(request);
            response.writeResponse(out);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
