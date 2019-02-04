package controller;

import dao.Model;
import dto.HttpRequest;
import dto.HttpResponse;

public class Home implements Controller {
    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        response.forward();
        return "index.html";
    }

}
