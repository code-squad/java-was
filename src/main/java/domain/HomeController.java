package domain;

import util.HttpRequestUtils;

import java.io.IOException;

public class HomeController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        doGet(request, response);
    }

    // TODO html파일만 반환하는 애들은 다 이런 로직을 가지는 데 어떻게 분리할 것인가?
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
