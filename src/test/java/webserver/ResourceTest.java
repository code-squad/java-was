package webserver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ResourceTest {
    private static final String REQUEST_LINE = "/index.html";
    private Resource resource;

    @Before
    public void setUp() {
        resource = Resource.of(REQUEST_LINE);
    }

    @Test
    public void getResource() throws Exception {
        byte[] page = resource.getBytes();
        assertTrue(new String(page).contains("SLiPP Java Web Programming"));
    }

}