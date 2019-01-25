package model;

import util.HttpRequestUtils;
import util.ParameterConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static model.Mapping.SPLIT_QUESTION;

public class RequestEntity {

    private static final String SPLIT_BLANK = " ";

    private Mapping mapping;
    private Map<String, String> body = new HashMap<>();
    private Map<String, String> headerInfo;

    public RequestEntity(String path, String method, String body, Map<String, String> headerInfo) {
        this.headerInfo = headerInfo;

        /* GET URL Method Parameter 존재할 경우에만 동작 */
        if(path.contains(Mapping.QUESTION_MARK)) {
           body = path.split(SPLIT_QUESTION)[1];
        }

        this.body = HttpRequestUtils.parseQueryString(body);
        this.mapping = Mapping.of(path, method);
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
