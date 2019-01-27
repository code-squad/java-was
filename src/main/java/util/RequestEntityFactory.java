package util;

import model.MethodType;
import model.RequestEntity;
import org.slf4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static org.slf4j.LoggerFactory.getLogger;

public class RequestEntityFactory {

    private static final Logger logger = getLogger(RequestEntityFactory.class);

    private static final String COLON = ":";

    public static RequestEntity of(BufferedReader br) throws IOException {
        /* Header 읽기 */
        String headerLine = br.readLine();
        String method = RequestEntity.obtainMethod(headerLine);
        String url = RequestEntity.obtainURL(headerLine);
        Map<String, String> header = readHeader(br);

        /* Body 읽기 */
        String body = readBody(obtainContentLength(header), br);

        return new RequestEntity(url, MethodType.obtainMethodType(method), body, header);
    }

    public static Map<String, String> readHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        String headerLine = "";
        while((headerLine = br.readLine()).trim().length() > 0) {
            /* [질문] JSESSIONID를 추출하는 부분 개선 필요! */
            if(headerLine.contains("JSESSIONID=")) {
                String[] line = headerLine.split(";");
                header.put(line[0].split(COLON)[0], line[0].split(COLON)[1].trim());
                header.put(line[1].trim().split("=")[0], line[1].trim().split("=")[1].trim());
            } else {
                header.put(headerLine.split(COLON)[0], headerLine.split(COLON)[1].trim());
            }

        }
        return header;
    }

    public static String readBody(int contentLength, BufferedReader br) throws IOException {
        String body = "";
        if(contentLength > 0) {
            char[] buf = new char[contentLength + 2]; // 2는 \r\n
            int read = br.read(buf);
            body = new String(buf);
        }
        return body;
    }

    public static int obtainContentLength(Map<String, String> headerInfo) {
        if(headerInfo.containsKey("Content-Length")) {
            return Integer.parseInt(headerInfo.get("Content-Length"));
        }

        return 0;
    }
}