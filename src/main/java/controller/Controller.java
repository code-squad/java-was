package controller;

import domain.HttpRequest;
import domain.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
