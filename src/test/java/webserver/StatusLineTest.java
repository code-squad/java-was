package webserver;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class StatusLineTest {
    private StatusLine statusLine;
    private final DataOutputStream dos = new DataOutputStream(System.out);

    @Test
    public void writeStatusLine_200() throws Exception {
        statusLine = new StatusLine(HttpStatus.OK);
        statusLine.writeStatusLine(dos);
    }

    @Test
    public void writeStatusLine_302() throws Exception {
        statusLine = new StatusLine(HttpStatus.FOUND);
        statusLine.writeStatusLine(dos);
    }
}