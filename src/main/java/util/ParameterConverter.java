package util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ParameterConverter {

    /*
       @param  URL 경로의 필드값
       @return 인코딩된 필드에 대해 디코딩하여 반환 (단, 디코딩되지 않으면 기존 필드 반환)
    */
    public static String urlDecoding(String field) {
        try {
            return URLDecoder.decode(field, "UTF-8");
        } catch (IllegalArgumentException | UnsupportedEncodingException e) {
            return field;
        }
    }
}
