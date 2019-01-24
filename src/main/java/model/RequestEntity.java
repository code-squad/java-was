package model;

import util.HttpRequestUtils;
import util.ParameterConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestEntity {

    private static final String SPLIT_BLANK = " ";
    private static final String SPLIT_QUESTION = "\\?";
    private static final String QUESTION_MARK = "?";

    private Mapping mapping;
    private Map<String, String> body = new HashMap<>();
    private Map<String, String> headerInfo;

    public RequestEntity(String path, String method, String body, Map<String, String> headerInfo) {
        this.headerInfo = headerInfo;

        /* POST Method Parameter 존재할 경우에만 동작 */
        if(!body.equals("")) {
           this.body = HttpRequestUtils.parseQueryString(body);
        }

        /* GET Method Parameter 존재할 경우에만 동작! */
        if(path.contains(QUESTION_MARK)) {
            path = initParams(path);
        }

        this.mapping = new Mapping(path, method);
    }

    private String initParams(String path) {
        this.body = HttpRequestUtils.parseQueryString(path.split(SPLIT_QUESTION)[1]);
        return path.split(SPLIT_QUESTION)[0];
    }

    public Mapping getMapping() {
        return mapping;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public String getJsessionId() {
        return headerInfo.get("JSESSIONID");
    }

    /*
       @param  Header의 첫 라인
       @return HttpHeader 에서 URL 추출
    */
    public static String obtainURL(String line) {
        return line.split(SPLIT_BLANK)[1];
    }

    /*
       @param  Header의 첫 라인
       @return HttpHeader 에서 Method 추출
    */
    public static String obtainMethod(String line) {
        return line.split(SPLIT_BLANK)[0];
    }

    /*
       @param
       @return 파라미터의 필드값 반환 (URL Encoding 처리된 문자는 URL Decoding)
    */
    public String obtainParamElement(String field) {
        if(!body.containsKey(field)) {
            return "";
        }
        return ParameterConverter.urlDecoding(body.get(field));
    }

    public void addHeader(String key, String value) {
        this.headerInfo.put(key, value);
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "mapping=" + mapping +
                ", body=" + body +
                ", headerInfo=" + headerInfo +
                '}';
    }

    public boolean isResource() {
        return headerInfo.get("Accept").contains("text/css");
    }

    public String obtainCookie() {
        return this.headerInfo.get("Cookie");
    }
}
