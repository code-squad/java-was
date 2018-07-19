package util;

import model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HeaderPathUtilsTest {
    private static final Logger log = LoggerFactory.getLogger(HeaderPathUtilsTest.class);

    @Test
    public void name() {
        String line = "GET /index.html HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);

        log.debug("path : {}", path);

        assertThat(path, is("/index.html"));
    }

    @Test
    public void fileRead() throws IOException {
        String line = "GET /index.html HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        String parsedBody = new String(body);

        log.debug("body : {}", parsedBody);

        assertTrue(parsedBody.contains("index.html"));
    }

    @Test
    public void extractParameter() throws UnsupportedEncodingException {
        String line = "GET /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);
        String query = HeaderPathUtils.extractQuery(path);
        User user = new User(query);
        User compareUser = new User("javajigi", "password", "pobi", "javajigi@slipp.net");

        assertThat(user, is(compareUser));
    }

    @Test
    public void containsQueryString() {
        String line = "GET /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);

        assertTrue(HeaderPathUtils.checkQueryString(path));
    }

    @Test
    public void notContainsQueryString() {
        String line = "GET /user/create HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);

        assertFalse(HeaderPathUtils.checkQueryString(path));
    }

    @Test
    public void realPath() {

        String line = "GET /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);
        String realPath = HeaderPathUtils.extractRealPath(path);

        log.debug("realPath : {}", realPath);

        assertThat(realPath, is("/user/create"));
    }

    @Test
    public void getQuery() {
        String line = "GET /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        String path = HeaderPathUtils.extractPath(line);
        String query = HeaderPathUtils.extractQuery(path);

        log.debug("realPath : {}", query);

        assertThat(query, is("userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net"));
    }

    @Test
    public void checkMethod_get() {
        String line = "GET /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        assertFalse(HeaderPathUtils.checkPost(line));
    }

    @Test
    public void checkMethod_post() {
        String line = "POST /user/create?userId=javajigi&password=password&name=pobi&email=javajigi@slipp.net HTTP/1.1\r\n";
        assertTrue(HeaderPathUtils.checkPost(line));
    }
}
