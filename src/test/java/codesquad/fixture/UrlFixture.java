package codesquad.fixture;

import codesquad.model.HttpMethod;
import codesquad.model.Url;

import java.util.HashMap;
import java.util.Map;

import static codesquad.fixture.UserFixture.USER;

public class UrlFixture {
    public static Url URL;
    public static Url URL2;
    public static Url URL3;

    static {
        Map<String, String> queryValue = new HashMap<>();
        queryValue.put("userId", USER.getUserId());
        queryValue.put("password", USER.getPassword());
        queryValue.put("name", USER.getName());
        queryValue.put("email", USER.getEmail());
        URL = new Url(HttpMethod.POST, "/user/create");

        Map<String, String> queryValue2 = new HashMap<>();
        queryValue2.put("userId", USER.getUserId());
        queryValue2.put("password", USER.getPassword());
        URL2 = new Url(HttpMethod.POST, "/user/create");

        Map<String, String> queryValue3 = new HashMap<>();
        queryValue3.put("user", USER.getUserId());
        queryValue3.put("password", USER.getPassword());
        URL3 = new Url(HttpMethod.POST, "/user/create");
    }
}
