package model;

import util.HttpRequestUtils;
import util.ParameterConverter;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final String SPLIT_BLANK = " ";
    private static final String QUESTION_MARK = "?";
    private static final String SPLIT_QUESTION = "\\?";

    private Mapping mapping;
    private HttpHeader httpHeader;
    private Map<String, String> body = new HashMap<>();

    public HttpRequest(String path, MethodType method, String body, HttpHeader httpHeader) {
        this.httpHeader = httpHeader;

        if(path.contains(QUESTION_MARK)) {
            body = path.split(SPLIT_QUESTION)[1];
        }

        this.body = HttpRequestUtils.parseQueryString(body);
        this.mapping = new Mapping(path, method);
    }

    public Mapping getMapping() {
        return mapping;
    }

    public Map<String, String> getBody() {
        return body;
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
       @return HttpHeader 에서 MethodType 추출
    */
    public static MethodType obtainMethod(String line) {
        return MethodType.obtainMethodType(line.split(SPLIT_BLANK)[0]);
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

    public String obtainHeader(String headerName) {
        return httpHeader.obtainHeader(headerName);
    }

    public void addHeader(String headerName, String headerValue) {
        httpHeader.addHeader(headerName, headerValue);
    }

    public boolean isResource() {
        return httpHeader.isResource();
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "mapping=" + mapping +
                ", body=" + body +
                '}';
    }
}
