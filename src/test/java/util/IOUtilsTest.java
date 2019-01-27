package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resource.UserFixture;
import webserver.ClientModel;

import static org.assertj.core.api.Assertions.assertThat;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    public void 패턴테스트() {
        assertThat(IOUtils.isPattern("{{writer.userId}}")).isTrue();
        assertThat(IOUtils.isPattern("{{#users}}")).isTrue();
        assertThat(IOUtils.isPattern("{{name}}")).isTrue();
    }

    @Test
    public void extractRegexTest1() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<String> accumulate = new ArrayList<>();
        accumulate.add("users");

        ClientModel clientModel = new ClientModel();
        clientModel.addAttribute("users", UserFixture.DOBY);

        String line = "<h1> {{userId}} </h1>";

        assertThat(IOUtils.extractRegex(line, accumulate, clientModel)).isEqualTo("<h1> doby </h1>");
    }

    @Test
    public void extractRegexTest2() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<String> accumulate = new ArrayList<>();

        ClientModel clientModel = new ClientModel();
        clientModel.addAttribute("users", UserFixture.DOBY);

        String line = "{{#users}}";

        assertThat(IOUtils.extractRegex(line, accumulate, clientModel)).isEqualTo("");
    }

    @Test
    public void extractRegexTest3() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<String> accumulate = new ArrayList<>();

        ClientModel clientModel = new ClientModel();
        clientModel.addAttribute("users", UserFixture.DOBY);

        String line = "{{/users}}";

        assertThat(IOUtils.extractRegex(line, accumulate, clientModel)).isEqualTo("");
    }

    @Test
    public void 정규식제거_성공() {
        assertThat(IOUtils.removeRegex("{{user}}")).isEqualTo("user");
        assertThat(IOUtils.removeRegex("{{user.id}}")).isEqualTo("user.id");
        assertThat(IOUtils.removeRegex("{{#user}}")).isEqualTo("#user");
    }

}
