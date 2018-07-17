import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ControllerTest {

//    @Test
//    public void parseUrl(){
//        String firstLine = "/user/create?userId=kang&password=1234&name=riverway&email=test@naver.com";
//        assertThat(Controller.getMethod(firstLine), is("/user/create"));
//    }
//
//    @Test
//    public void parseUrl_no_queryString(){
//        String firstLine = "/user/create";
//        assertThat(Controller.getMethod(firstLine), is("/user/create"));
//    }

    @Test
    public void mapTest() {
        Map<String, String> test = new HashMap<>();
        test.put("test", "1");
        test.put("test1", "2");
        test.put("test2", "3");

        System.out.println(test.get("test5"));
    }

//    @Test
//    public void stringBuilderTest() throws Exception {
//        ModelAndView modelAndView = new ModelAndView("/user/list.html");
//        User user = new User("riverway", "password", "kang", "rlfghks@naver.com");
//        modelAndView.setAttribute("user", user);
//        modelAndView.transfer();
//        String model = "{{#a}}";
//        String model2 = "{{/a}}";
//
//        String start = body1.substring(0, body1.indexOf(model));
//        System.out.println("Start : " + start);
//        String result = body1.substring(body1.indexOf(model), body1.indexOf(model2));
//
//
//
//
//        System.out.println(result);
//        String last = body1.substring(body1.indexOf(model2));
//        System.out.println(last);
//        StringBuilder sb = new StringBuilder();
//    }
}
