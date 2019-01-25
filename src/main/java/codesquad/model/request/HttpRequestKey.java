package codesquad.model.request;

import com.google.common.base.Strings;

public enum HttpRequestKey {

    HTTP_METHOD("HttpMethod"),
    QUERY_VALUE("QueryValue"),
    ACCESS_PATH("AccessPath"),
    CONTENT_LENGTH("Content-Length"),
    COOKIE("Cookie"),
    HOST("Host"),
    CONNECTION("Connection"),
    CACHE_CONTROL("Cache-Control"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    ACCEPT_ENCODING("Accept-Encoding"),
    ACCEPT_LANGUAGE("Accept-Language"),
    REFERER("Referer"),
    ORIGIN("Origin"),
    ETC("etc");

    String key;

    HttpRequestKey(String key) {
        this.key = key;
    }

    public static HttpRequestKey of(String key) {
        if(Strings.isNullOrEmpty(key)) return ETC;
        return getHttpRequest(key);
    }

    private static HttpRequestKey getHttpRequest(String key) {
        for (HttpRequestKey value : values()) {
            if(value.key.equals(key)) return value;
        }
        return ETC;
    }
}
