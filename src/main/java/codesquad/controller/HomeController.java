package codesquad.controller;

import codesquad.Controller;
import codesquad.RequestMapping;
import codesquad.model.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "/index.html";
    }
}
