package model;


import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseTest {
	private static final Logger log = LoggerFactory.getLogger(HttpResponseTest.class);
	private String testDirectory = "./src/test/java/";
	
	@Test
    public void responseForward() throws Exception {
        // Http_Forward.txt 결과는 응답 body에 index.html이 포함되어 있어야 한다.
        HttpResponse response = new HttpResponse(new FileOutputStream(new File(testDirectory + "Http_Forward.txt")));
        response.forward("/index.html");    

        
    }
	
	
}
