package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.RequestMapping;

public class AbstractController implements Controller {

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        RequestMapping requestMapping = new RequestMapping();
        AbstractController controller = requestMapping.getController(request.getPath());

        if (request.isGet()) {
            controller.doGet(request, response);
        }

        if (request.isPost()) {
            controller.doPost(request, response);
        }
    }

    void doPost(HttpRequest request, HttpResponse response) {
    }

    void doGet(HttpRequest request, HttpResponse response) {
    }
}
