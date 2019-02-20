package webserver;

import controller.Controller;
import dao.Model;
import dto.HttpRequest;
import dto.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DispatchResolver;
import util.HttpParser;
import view.ViewResolver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest req = HttpParser.parse(in);
            HttpResponse res = new HttpResponse();
            Model model = new Model();

            Controller controller = DispatchResolver.get(req);
            String viewName = controller.service(req, res, model);
            byte[] view = ViewResolver.resolve(viewName, model);

            send(res, view, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void send(HttpResponse response, byte[] view, OutputStream out) {
        if (view.length > 0) {
            response.setHeader("Content-Length", String.valueOf(view.length));
        }

        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeBytes(response.toString());
            dos.write(view, 0, view.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
