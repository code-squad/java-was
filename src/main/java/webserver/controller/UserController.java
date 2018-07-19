package webserver.controller;

import model.User;
import webserver.HttpStatus;
import webserver.annotation.RequestMapping;
import webserver.request.Request;
import webserver.response.Response;
import webserver.support.controller.ModelInitializer;

import static webserver.HttpMethod.GET;
import static webserver.response.ResponseHeaderAttribute.LOCATION;

@RequestMapping("/user")
public class UserController implements Controller {

    private UserController() {
    }

    @Override
    @RequestMapping(method = GET, value = "/create")
    public Response process(Request request, Response response) {
        if (ModelInitializer.init(request.getParameters(), User.class).isPresent()) {
            return response.setStatus(HttpStatus.FOUND).setHeader(LOCATION, "/index.html").setBody("");
        }
        return response.setStatus(HttpStatus.BAD_REQUEST).setBody("");
    }
}
