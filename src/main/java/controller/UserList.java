package controller;

import dao.Model;
import db.DataBase;
import dto.HttpRequest;
import dto.HttpResponse;
import model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserList implements Controller {
    @Override
    public String service(HttpRequest request, HttpResponse response, Model model) {
        String dbg = request.header("Cookie");
        if (request.header("Cookie").contains("true")) {
            Collection<User> savedUsers = DataBase.findAll();
            List<List<String>> values = savedUsers.stream().map((u)->{
                ArrayList<String> list = new ArrayList<>();
                list.add(u.getName());
                list.add(u.getUserId());
                list.add(u.getPassword());
                return list;
            }).collect(Collectors.toList());
            model.setAttribute("user", values);
            response.forward();
            return "/user/list.html";
        }
        response.redirect("/index.html");
        return "redirect";
    }
}
