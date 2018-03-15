package webserver;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RequestLineTest {
    private String getRequestLine;
    private RequestLine line;


    @Before
    public void setUp() throws Exception {
        getRequestLine = "GET /user/create?userId=chloe&password=password&name=JaeSung HTTP/1.1";
    }

    @Test
    public void getParameter() throws Exception {
        line = new RequestLine(getRequestLine);
        assertEquals("chloe", line.getParameter("userId"));
    }

    @Test
    public void getURI() {
        line = new RequestLine(getRequestLine);
        assertEquals("/user/create?userId=chloe&password=password&name=JaeSung", line.getURI());
    }
}
