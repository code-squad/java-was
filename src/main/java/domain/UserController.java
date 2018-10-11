package domain;

import util.HttpRequestUtils;

import java.io.IOException;

public class UserController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        byte[] body = new byte[0];
        try {
            body = HttpRequestUtils.readFile(request.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.response200Header(body.length).responseBody(body);
    }
}
