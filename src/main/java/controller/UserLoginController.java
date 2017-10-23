package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import header.HttpHeader;
import header.HttpRedirectHeaderWithLoginedCookie;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.WebServer;

public class UserLoginController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
    private static DataBase db = WebServer.db;
    private HttpHeader header;

    @Override
    public void run(HttpRequest req, HttpResponse res) {
        log.debug("fuck you.");
        if(isLoginTrue(req.getRequestBody())) {
            this.header = new HttpRedirectHeaderWithLoginedCookie("/index.html", true);
            res.setHeader(this.header.generateHttpHeaderString());
            res.responseHeaderWithoutBody();
        }
        else {
            this.header = new HttpRedirectHeaderWithLoginedCookie("/user/login_failed.html", false);
            res.setHeader(this.header.generateHttpHeaderString());
            res.responseHeaderWithoutBody();
        }
        

    }

    private boolean isLoginTrue(String loginRequestInfo) {
        User requestedLoginUser = User.createNewUser(HttpRequestUtils.parseQueryString(loginRequestInfo));
        User targetUser = db.findUserById(requestedLoginUser.getUserId());

        return targetUser.isLoginInfoCorrect(requestedLoginUser.getPassword());

    }

}
