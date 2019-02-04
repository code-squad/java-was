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

    public RequestHandler(Socket connectionSocket) {
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
//            byte[] view = ViewResolver.resolve(viewName, model);
//            res.setBody(view);


//
//            List<String> request = new ArrayList<>();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//            String line;
//            while (!"".equals(line = br.readLine())) {
//                if (line == null) {
//                    return;
//                }
//                log.debug("{}", line);
//                request.add(line);
//            }
//
//            String[] startLine = request.get(0).split(" ");
//            String method = startLine[0];
//            String requestUrl = startLine[1];
//            String version = startLine[2];
//
//            Map<String, String> headers = HttpRequestUtils.parseHeader(request.subList(1, request.size()));
//
//            String[] splitResource = requestUrl.split("\\?");
//            String url = splitResource[0];
//            String queryString = "";
//            if (splitResource.length == 2) {
//                queryString = URLDecoder.decode(splitResource[1], "UTF-8");
//            }
//
//
//            log.debug("requestUrl : {} , url : {}, queryString : {}", requestUrl, url, queryString);
//
//
//            byte[] responseBody = "".getBytes();
//            DataOutputStream dos = new DataOutputStream(out);
//
//            if (method.equals("GET")) {
//                if (url.equals("/user/list.html")) {
//                    System.out.println("list");
//                    if (headers.get("Cookie") != null && headers.get("Cookie").contains("true")) {
//                        BufferedReader fr = new BufferedReader(new FileReader("./webapp/user/list.html"));
//                        StringBuilder sb = new StringBuilder();
//                        String fileLine;
//                        while ((fileLine = fr.readLine()) != null) {
//                            if (fileLine.contains("{{list}}")) {
//                                StringBuilder iterateSb = new StringBuilder();
//                                String iterateLine;
//                                while (!((iterateLine = fr.readLine()).contains("{{/list}}"))) {
//                                    iterateSb.append(iterateLine);
//                                }
//                                String forString = iterateSb.toString();
//                                for (User user : DataBase.findAll()) {
//                                    sb.append(forString
//                                            .replace("{{id}}", user.getUserId())
//                                            .replace("{{name}}", user.getName())
//                                            .replace("{{email}}", user.getEmail()));
//                                }
//                            } else {
//                                sb.append(fileLine);
//                            }
//                        }
//                        responseBody = sb.toString().getBytes();
//                    } else {
//                        response304Header(dos, "/index.html");
//                    }
//                } else {
//                    responseBody = Files.readAllBytes(new File("./webapp" + url).toPath());
//                }
//                if (headers.get("Accept").contains("css")) {
//                    response200HeaderCss(dos, responseBody.length);
//                }else{
//                    response200Header(dos, responseBody.length);
//                }
//                responseBody(dos, responseBody);
//            } else if (method.equals("POST")) {
//                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
//                Map<String, String> ret = HttpRequestUtils.parseQueryString(requestBody);
//                if (url.equals("/user/create")) {
//                    DataBase.addUser(
//                            new User(ret.get("userId"),
//                                    ret.get("password"),
//                                    ret.get("name"),
//                                    ret.get("email"))
//                    );
//                    System.out.println("post");
//                    log.debug("query body : {}", ret);
//                    response304Header(dos, "/index.html");
//                } else if (url.equals("/user/login")) {
//                    if (DataBase.findUserById(ret.get("userId"))
//                            .map(User::getPassword)
//                            .filter((x) -> x.equals(ret.get("password")))
//                            .isPresent()) {
//                        response304HeaderLogined(dos, "/index.html", true);
//                    } else {
//                        response304HeaderLogined(dos, "/index.html", false);
//                    }
//                }
//            }

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


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200HeaderCss(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response304HeaderLogined(DataOutputStream dos, String location, boolean logined) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Set-Cookie: logined=" + logined + "; Path=/");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response304Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
