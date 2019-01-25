package support;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BufferedReaderGenerator {
    public static BufferedReader generateBufferedReader(String value) {
        InputStream is = new ByteArrayInputStream(value.getBytes());
        return new BufferedReader(new InputStreamReader(is));
    }
}
