package controller;

import domain.HttpRequest;
import domain.HttpResponse;

abstract class AbstractController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        if (request.getRequestLine("method").equals("GET")) {
            doGet(request, response);
            return;
        }
        doPost(request, response);
    }

    protected void doGet(HttpRequest request, HttpResponse response) {

    }

    protected void doPost(HttpRequest request, HttpResponse response) {

    }
}
