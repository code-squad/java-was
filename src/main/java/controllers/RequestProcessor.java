package controllers;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface RequestProcessor {

    HttpResponse processRequest(HttpRequest request);
}
