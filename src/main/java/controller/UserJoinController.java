package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import header.HttpHeader;
import header.HttpRedirectHeader;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.WebServer;

public class UserJoinController extends PostController {

    private static final Logger log = LoggerFactory.getLogger(UserJoinController.class);
    DataBase db = WebServer.db;
    HttpHeader header;

    @Override
    public void run(HttpRequest req, HttpResponse res) {

        String userinfo = req.getRequestBody();
        log.error("this is user info: " + userinfo);
        addUser(userinfo);
        header = new HttpRedirectHeader("/index.html");
        res.setHeader(header.generateHttpHeaderString());
        res.responseHeaderWithoutBody();

    }

    private void addUser(String userdata) {
        db.addUser(User.createNewUser(HttpRequestUtils.parseQueryString(userdata)));
        db.findAll().stream().forEach(u -> log.debug(u.toString()));
        this.header = new HttpRedirectHeader("/index.html");
    }

}
