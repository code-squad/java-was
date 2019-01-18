package model;

import util.HttpRequestUtils;
import util.ParameterConverter;
import java.util.Map;
import java.util.Objects;

public class URLInfo {

    private static final String SPLIT_BLANK = " ";
    private static final String SPLIT_QUESTION = "\\?";
    private static final String QUESTION_MARK = "?";
    private static final String ROOT = "./webapp";

    private String path;
    private Map<String, String> params;
    private String method;

    public URLInfo(String path, String method) {
        this.path = path;
        this.method = method;

        if(path.contains(QUESTION_MARK)) {
            initParams(path);
        }
    }

    private void initParams(String path) {
        this.path = path.split(SPLIT_QUESTION)[0];
        this.params = HttpRequestUtils.parseQueryString(path.split(SPLIT_QUESTION)[1]);
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
        if(!params.containsKey(field)) {
            return "";
        }
        return ParameterConverter.urlDecoding(params.get(field));
    }

    @Override
    public String toString() {
        return "URLInfo{" +
                "path='" + path + '\'' +
                ", params=" + params +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLInfo urlInfo = (URLInfo) o;
        return Objects.equals(path, urlInfo.path) &&
                Objects.equals(method, urlInfo.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, method);
    }
}
