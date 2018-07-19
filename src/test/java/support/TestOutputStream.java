package support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestOutputStream extends OutputStream {
    private static final Logger log = LoggerFactory.getLogger(TestOutputStream.class);

    private static final int NEXT_LINE = 10;
    private List<Byte> data = new ArrayList<>();

    @Override
    public void write(int b) throws IOException {
        data.add((byte) b);
        if (b == NEXT_LINE) {
            log.debug("data : {}", data.stream().map(byteData -> String.valueOf((char) byteData.byteValue())).reduce("", String::concat));
            data.clear();
        }
    }
}
