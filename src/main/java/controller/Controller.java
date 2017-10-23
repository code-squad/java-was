package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
    
    public void run(HttpRequest req, HttpResponse res);
}
