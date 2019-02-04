package controller;

import dao.Model;
import dto.HttpRequest;
import dto.HttpResponse;

public interface Controller {
    String service(HttpRequest request, HttpResponse response, Model model);

}
