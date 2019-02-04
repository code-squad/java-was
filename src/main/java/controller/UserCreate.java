package controller;

import dao.Model;
import db.DataBase;
import dto.HttpRequest;
import dto.HttpResponse;
import model.User;

public class UserCreate implements Controller {
    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        User user = new User(request.query("userId"),
                request.query("password"),
                request.query("name"),
                request.query("email"));
        DataBase.addUser(user);
        response.redirect("/index.html");
        return "redirect";
    }
}
