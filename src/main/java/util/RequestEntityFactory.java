package util;

import model.RequestEntity;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class RequestEntityFactory {

    private static final Logger logger = getLogger(RequestEntityFactory.class);

    public static RequestEntity of(BufferedReader br) {
        String method = "";
        String param = "";
        String url = "";
        try {
            String headerLine = br.readLine();
            logger.debug("Request Header Line : {}", headerLine);
            method = RequestEntity.obtainMethod(headerLine);
            url = RequestEntity.obtainURL(headerLine);

            while(!(headerLine = br.readLine()).equals("")) {
                logger.debug("Request Header Line : {}", headerLine);
            }
            param = headerLine;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RequestEntity(url, method, param);
    }
}
