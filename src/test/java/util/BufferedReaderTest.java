package util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BufferedReaderTest {
    private static final Logger log =  LoggerFactory.getLogger(BufferedReaderTest.class);
    
    @Test
    public void read() throws IOException {
        String message = "request line" + "\n" + "contentType:image/jpeg" + "\n" + "\n" + "this is body man";

        InputStream is = new ByteArrayInputStream(message.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));


        String line = null;

        while((line = reader.readLine()) != null) {
            log.debug("readline : {}", line);
        }
    }
}
