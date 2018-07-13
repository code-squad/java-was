import org.junit.Test;
import webserver.Controller;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
    public void mapTest(){
        Map<String, String> test = new HashMap<>();
        test.put("test", "1");
        test.put("test1", "2");
        test.put("test2", "3");

        System.out.println(test.get("test5"));
    }
}
