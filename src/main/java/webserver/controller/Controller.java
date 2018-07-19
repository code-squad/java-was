package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

public interface Controller {

    Response process(Request request, Response response);
}
