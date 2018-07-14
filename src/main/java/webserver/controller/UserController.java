package webserver.controller;

import model.User;
import webserver.HttpStatus;
import webserver.annotation.RequestMapping;
import webserver.request.Request;
import webserver.response.Response;
import webserver.support.controller.ModelInitializer;

import static webserver.response.ResponseHeaderAttribute.LOCATION;

@RequestMapping("/user")
public class UserController implements Controller {

    private UserController() {
    }

    /* TODO : 리팩토링 - 지금 유저를 생성하는 것 밖에 못함(맵핑을 더 디데일하게) */
    @RequestMapping("/create")
    public Response process(Request request, Response response) {
        if (ModelInitializer.init(request.getParameters(), User.class).isPresent()) {
            return response.setStatus(HttpStatus.FOUND).setHeader(LOCATION, "/index.html").setBody("");
        }
        return response.setStatus(HttpStatus.BAD_REQUEST).setBody("");
    }
}
