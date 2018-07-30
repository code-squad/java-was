package com.larry.webserver.http;

import java.util.Objects;

public class RequestHandlerKey {
    private String url;
    private HttpMethod requestMethod;

    public RequestHandlerKey(String url, HttpMethod requestMethod) {
        this.url = url;
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestHandlerKey that = (RequestHandlerKey) o;
        return Objects.equals(url, that.url) &&
                requestMethod == that.requestMethod;
    }

    @Override
    public int hashCode() {

        return Objects.hash(url, requestMethod);
    }
}