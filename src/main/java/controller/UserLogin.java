package controller;

import dao.Model;
import db.DataBase;
import dto.HttpRequest;
import dto.HttpResponse;
import model.User;

import java.util.Optional;


public class UserLogin implements Controller {
    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        Optional<User> savedUser = DataBase.findUserById(request.query("userId"));

        if (savedUser.isPresent() && savedUser.get().isSamePassword(request.query("password"))) {
            response.setHeader("Set-Cookie", "logined=true; Path=/");
        }else{
            response.setHeader("Set-Cookie", "logined=false; Path=/");
        }

        response.redirect("/index.html");
        return "redirect";
    }
}
