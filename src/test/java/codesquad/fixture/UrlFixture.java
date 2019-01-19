package codesquad.fixture;

import codesquad.model.RequestMethod;
import codesquad.model.Url;

import java.util.HashMap;
import java.util.Map;

import static codesquad.fixture.UserFixture.USER;

public class UrlFixture {
    public static Url URL;

    static {
        Map<String, String> queryValue = new HashMap<>();
        queryValue.put("userId", USER.getUserId());
        queryValue.put("password", USER.getPassword());
        queryValue.put("name", USER.getName());
        queryValue.put("email", USER.getEmail());
        URL = new Url(RequestMethod.POST, "/user/create", queryValue);
    }
}
