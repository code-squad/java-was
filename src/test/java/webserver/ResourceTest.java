package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {
    private static final String REQUEST_LINE = "GET /index.html HTTP/1.1";
    private Resource resource;

    @Before
    public void setUp() {
        resource = new Resource(REQUEST_LINE);
    }

    @Test
    public void getResource() throws Exception {
        byte[] page = resource.getContent();
        assertTrue(new String(page).contains("SLiPP Java Web Programming"));
    }

}