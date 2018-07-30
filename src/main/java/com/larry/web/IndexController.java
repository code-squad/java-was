package com.larry.web;

import com.larry.webserver.annotations.Controller;
import com.larry.webserver.annotations.RequestMapping;
import com.larry.webserver.http.HttpMethod;
import com.larry.webserver.http.Request;
import com.larry.webserver.http.Response;
import com.larry.webserver.mvc.viewFlow.ModelAndView;

import static com.larry.webserver.http.HttpMethod.GET;

@Controller
public class IndexController {

    @RequestMapping(method = GET, path = "/index.html")
    public ModelAndView index(Request request, Response response) {
        return ModelAndView.viewOf("index.html");
    }

}
