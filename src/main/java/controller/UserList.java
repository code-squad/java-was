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
        if (request.isTrueCookie("logined")) {
            Collection<User> savedUsers = DataBase.findAll();
            model.setAttribute("user", AdaptToModel(savedUsers));
            response.forward();
            return "/user/list.html";
        }
        response.redirect("/index.html");
        return "redirect";
    }

    private List<List<String>> AdaptToModel(Collection<User> savedUsers) {
        return savedUsers.stream()
                        .map((u) -> {
                            ArrayList<String> list = new ArrayList<>();
                            list.add(u.getName());
                            list.add(u.getUserId());
                            list.add(u.getPassword());
                            return list;
                        })
                        .collect(Collectors.toList());
    }
}
