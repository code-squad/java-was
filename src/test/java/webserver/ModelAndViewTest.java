package webserver;

import db.DataBase;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ModelAndViewTest {

    private ModelAndView modelAndView;

    @Before
    public void setUp() throws Exception {
        modelAndView = new ModelAndView("/user/list.html");
        modelAndView.setAttribute("user", new User("riverway", "password", "kang", "rlfghks@naver.com"));
        modelAndView.setAttribute("user2", new User("pobi", "password", "javajigi", "test@naver.com"));
    }

    @Test
    public void getKeyTest(){
        List<String> keys = modelAndView.getKey();
        assertTrue(keys.contains("user"));
        assertTrue(keys.contains("user2"));
    }

    @Test
    public void getValueTest(){
        List<String> keys = modelAndView.getKey();
        for (String key : keys){
            User user = (User) modelAndView.getAttribute(key);
            assertTrue(user.getUserId().equals("pobi") || user.getUserId().equals("riverway"));
        }
    }

    @Test
    public void isEmptyTest(){
        assertFalse(modelAndView.isEmpty());
    }

    @Test
    public void keyTransfer(){
        assertThat(modelAndView.keyStart("user"), is("{{#user}}"));
        assertThat(modelAndView.keyFinsh("user"), is("{{/user}}"));
    }

    @Test
    public void invokeTest() throws Exception{
        String test = "{{userId}}test{{name}}";
        String userId = test.substring(test.indexOf("{{") + 2, test.indexOf("}}"));
        assertThat(userId, is("userId"));
        userId = modelAndView.invoke(userId, modelAndView.getAttribute("user"));
        assertThat(userId, is("riverway"));
    }

    @Test
    public void changeValueTest() throws Exception{
        String test = "{{userId}} test {{name}}";
        String result = modelAndView.changeValue(test, modelAndView.getAttribute("user"));
        assertThat(result, is("riverway test kang"));
    }

    @Test
    public void noCollectionTest() throws Exception{
        String result = modelAndView.transfer();
        System.out.println(result);
        assertTrue(result.contains("riverway"));
        assertTrue(result.contains("kang"));
        assertFalse(result.contains("{{#user}}"));
    }

    @Test
    public void collectionTest() throws Exception{
        modelAndView = new ModelAndView("/user/list.html");

        DataBase.addUser(new User("riverway", "password", "kang", "rlfghks@naver.com"));
        DataBase.addUser(new User("pobi", "password", "javajigi", "test@naver.com"));
        DataBase.addUser(new User("code", "password", "code", "code@naver.com"));
        modelAndView.setAttribute("user", DataBase.findAll());

        String result = modelAndView.transfer();
        System.out.println(result);

        assertTrue(result.contains("riverway"));
        assertTrue(result.contains("pobi"));
        assertTrue(result.contains("code"));
        assertFalse(result.contains("{{#user}}"));
    }
}
