package controller;

import util.HttpRequestUtils.RequestTypes;
import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
    
    public void run(HttpRequest req, HttpResponse res);
    public RequestTypes getType();
}
