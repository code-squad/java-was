package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.HttpMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = HttpMethod.GET)
    public String home() {
        return "/index.html";
    }
}
