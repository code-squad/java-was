package util;

import model.RequestHeader;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.slf4j.LoggerFactory.getLogger;

public class RequestHeaderFactory {

    private static final Logger logger = getLogger(RequestHeaderFactory.class);

    public static RequestHeader of(InputStream in) {
        String headerLine = null;
        String method = null;
        String param = null;
        String url = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            headerLine = br.readLine();
            method = RequestHeader.obtainMethod(headerLine);
            url = RequestHeader.obtainURL(headerLine);

            if(method.equals("POST")) {
                /*while(!(headerLine = br.readLine()).equals("")) {
                    logger.debug("Request Header Line : {}", headerLine);
                }*/
                //param = br.readLine();
                while(!(headerLine = br.readLine()).equals("")) {
                    logger.debug("Request Header Line : {}", headerLine);
                }
                param = headerLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RequestHeader(url, method, param);
    }
}
