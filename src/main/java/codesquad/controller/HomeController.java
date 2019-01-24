package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.request.HttpMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = HttpMethod.GET)
    public String home() {
        return "/index.html";
    }
}
