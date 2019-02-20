package controller;

import dao.Model;
import dto.HttpRequest;
import dto.HttpResponse;

public class StyleSheet implements Controller {

    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        response.stylesheet();
        return request.url();
    }
}
