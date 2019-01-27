package util;

import model.HttpHeader;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpHeaderFactory {

    private static final Logger logger = getLogger(HttpHeaderFactory.class);

    private static final String COLON = ":";

    private static final String JSESSION_SPLIT = "JSESSIONID=";

    public static HttpHeader of(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        String headerLine = "";
        while((headerLine = br.readLine()) != null && headerLine.trim().length() > 0) {
            /* [질문] JSESSIONID를 추출하는 부분 개선 필요! */
            if(headerLine.contains(JSESSION_SPLIT)) {
                String[] line = headerLine.split(";");
                header.put(line[0].split(COLON)[0], line[0].split(COLON)[1].trim());
                header.put(line[1].trim().split("=")[0], line[1].trim().split("=")[1].trim());
            }

            if(!headerLine.contains(JSESSION_SPLIT)) {
                header.put(headerLine.split(COLON)[0], headerLine.split(COLON)[1].trim());
            }

        }
        return new HttpHeader(header);
    }
}
