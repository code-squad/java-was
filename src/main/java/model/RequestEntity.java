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

    private String path;
    private Map<String, String> body = new HashMap<>();
    private String method;
    private Map<String, String> headerInfo;

    public RequestEntity(String path, String method, String body, Map<String, String> headerInfo) {
        this.path = path;
        this.method = method;
        this.headerInfo = headerInfo;

        /* POST Method Parameter 존재할 경우에만 동작 */
        if(!body.equals("")) {
           this.body = HttpRequestUtils.parseQueryString(body);
        }

        /* GET Method Parameter 존재할 경우에만 동작! */
        if(path.contains(QUESTION_MARK)) {
            initParams(path);
        }
    }

    private void initParams(String path) {
        this.path = path.split(SPLIT_QUESTION)[0];
        this.body = HttpRequestUtils.parseQueryString(path.split(SPLIT_QUESTION)[1]);
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

    /*
       @param Cookie 명
       @return 쿠키 등록 후, 체인방식을 위해 본인 리턴
    */
    public RequestEntity addCookie(String cookie) {
        headerInfo.put("Cookie", cookie);
        return this;
    }

    /*
       @param
       @return 쿠키 등록 유무 확인
    */
    public boolean hasLoginLoCookie() {
        if(headerInfo != null && headerInfo.containsKey("Cookie")) {
            return headerInfo.get("Cookie").contains("logined=true");
        }
        return false;
    }

    public boolean hasHeader(String headerName) {
        return this.headerInfo.containsKey(headerName);
    }

    public String obtainCookie() {
        return headerInfo.get("Cookie");
    }

    @Override
    public String toString() {
        return "RequestEntity{" +
                "path='" + path + '\'' +
                ", body=" + body +
                ", method='" + method + '\'' +
                ", headerInfo=" + headerInfo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEntity urlInfo = (RequestEntity) o;
        return Objects.equals(path, urlInfo.path) &&
                Objects.equals(method, urlInfo.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
