package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private RequestPath path;
    private RequestHeaders headers;
    private RequestBody body;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String data = br.readLine();
        log.debug("requestLine : {}", data);
        while (!data.equals("")) {
            data = br.readLine();
            if (data == null) {
                break;
            }
            log.debug("requestHeader : {}", data);
        }
    }
}
