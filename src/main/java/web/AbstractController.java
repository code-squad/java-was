package web;

import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {

    public void service(HttpRequest request, HttpResponse response){
        if (request.isGetMethod()){
            doGet(request, response);
            return;
        }
        doPost(request, response);
    }

    abstract void doGet(HttpRequest request, HttpResponse response);
    abstract void doPost(HttpRequest request, HttpResponse response);
}
