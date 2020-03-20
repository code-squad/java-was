//package http;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import util.HttpResponseUtils;
//
//import static org.junit.Assert.*;
//
//public class HttpResponseTest {
//    private static final Logger log = LoggerFactory.getLogger(HttpResponseTest.class);
//    HttpResponse httpResponse;
//
//    @Before
//    public void 객체생성() {
//        httpResponse = new HttpResponse();
//        httpResponse.("Content-Type", "text/html;charset=utf-8");
//        httpResponse.addHeader("Content-Length", "123");
//    }
//
//
//    @Test
//    public void processHeaders() {
//        log.error("headers : {} ",httpResponse.processHeaders());
//    }
//}
