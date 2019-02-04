package controller;

import dao.Model;
import dto.HttpRequest;
import dto.HttpResponse;

public class StaticControl implements Controller {
    private static StaticControl staticControl;

    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        response.stylesheet();
        return request.url();
    }
}
