package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;
import controller.IndexController;
import controller.ResourceController;
import db.DataBase;
import model.User;
import requestmapping.RequestLine;
import requestmapping.RequestLineFactory;
import requestmapping.RequestMapping;

import java.nio.file.*;

import util.HttpRequestUtils;
import util.HttpRequestUtils.RequestTypes;
import util.IOUtils;
import util.StringUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final RequestLine notFoundRequestLine = RequestLineFactory.generateRequestLine(RequestTypes.GET,
            "/resource");

    private Socket connection;
    private RequestMapping rm;

    private boolean login;

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
            log.debug(rl.getPath());
            log.debug(rl.getMethod().toString());

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
