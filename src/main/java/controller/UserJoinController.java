package controller;

import db.DataBase;
import header.HttpHeader;
import header.HttpRedirectHeader;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.WebServer;

public class UserJoinController extends PostController {

    DataBase db = WebServer.db;
    HttpHeader header;

    @Override
    public void run(HttpRequest req, HttpResponse res) {

        String userinfo = req.getRequestBody();
        addUser(userinfo);
        res.setHeader(header.generateHttpHeaderString());

    }

    private void addUser(String userdata) {
        db.addUser(User.createNewUser(HttpRequestUtils.parseQueryString(userdata)));
        this.header = new HttpRedirectHeader("/index.html");
    }

}
