package web;

import webserver.RequestMapping;

public class HttpController {

    @RequestMapping(method = "GET", path = "/index.html")
    public String index() {
        return "index.html";
    }

}
