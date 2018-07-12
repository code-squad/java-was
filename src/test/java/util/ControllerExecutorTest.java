package util;

import org.junit.Test;
import web.HttpController;
import webserver.ControllerExecutor;
import webserver.Request;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ControllerExecutorTest {

    @Test
    public void getViewName_index() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/indexGet.http"));
        Request request = new Request(br);
        String viewFileName = ControllerExecutor.execute(HttpController.class, request);
        assertThat(viewFileName, is("index.html"));
    }

    @Test
    public void getViewName_create_user() throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException {
        BufferedReader br = new BufferedReader(new FileReader("/Users/chaegyunjung/Documents/codesquad/java-was/http-requests/userCreatePost.http"));
        Request request = new Request(br);
        String viewFileName = ControllerExecutor.execute(HttpController.class, request);
        assertThat(viewFileName, is("redirect:/index.html"));
    }

}
