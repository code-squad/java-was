package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestGenerator;
import webserver.http.request.HttpRequest;
import model.User;
import org.junit.Test;
import support.BufferedReaderGenerator;

import java.io.BufferedReader;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class ObjectMakerTest {

    private static final Logger logger = LoggerFactory.getLogger(ObjectMakerTest.class);

    @Test
    public void makeNewUser() throws IOException {
        String value = "POST /user/create HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 59\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";

        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpRequest request = RequestGenerator.generateRequest(br);

        User user = ObjectMaker.makeNewUser(request);
        assertThat(user.getUserId(), is("javajigi"));
    }

    @Test
    public void makeLoginUser() throws IOException {
        String value = "POST /user/login HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 33\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "Accept: */*\n" +
                "\n" +
                "userId=javajigi&password=password";

        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        HttpRequest request = RequestGenerator.generateRequest(br);

        User user = ObjectMaker.makeNewUser(request);
        assertThat(user.getUserId(), is("javajigi"));

        logger.debug("name : {}", user.getName());
        logger.debug("email : {}", user.getEmail());
    }
}