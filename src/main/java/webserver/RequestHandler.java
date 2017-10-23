package webserver;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.ResourceController;
import requestmapping.RequestLine;
import requestmapping.RequestLineFactory;
import requestmapping.RequestMapping;
import util.HttpRequestUtils.RequestTypes;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);


    private Socket connection;
    private RequestMapping rm;


    public RequestHandler(Socket connectionSocket, RequestMapping rm) {
        this.connection = connectionSocket;
        this.rm = rm;

    }

    public void run() {

        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest req = new HttpRequest(in);
            HttpResponse res = new HttpResponse(new DataOutputStream(out));
            RequestLine rl = req.getLine();

            Controller controller = rm.getController(rl);

            if (controller == null) {
                controller = new ResourceController();
                controller.run(req, res);
            } else {

                controller.run(req, res);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
