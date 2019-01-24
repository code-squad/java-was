package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.RequestBody;
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
        String value = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        RequestBody requestBody = HttpBodyUtils.parseRequestBody(br, 59);

        User user = ObjectMaker.makeNewUser(requestBody);
        assertThat(user.getUserId(), is("javajigi"));
    }

    @Test
    public void makeLoginUser() throws IOException {
        String value = "userId=javajigi&password=password";
        BufferedReader br = BufferedReaderGenerator.generateBufferedReader(value);
        RequestBody requestBody = HttpBodyUtils.parseRequestBody(br, 33);

        User user = ObjectMaker.makeNewUser(requestBody);
        assertThat(user.getUserId(), is("javajigi"));

        logger.debug("name : {}", user.getName());
        logger.debug("email : {}", user.getEmail());
    }
}