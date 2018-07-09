package model;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;

public class RequestTest {
    private static final Logger log = LoggerFactory.getLogger(RequestTest.class);

    private Request request;

    @Before
    public void setUp() throws Exception {
        request = new Request(new FileInputStream(new File(getClass().getClassLoader().getResource("request.txt").getFile())));
    }

    @Test
    public void requestInit() {

    }
}
