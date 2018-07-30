package com.larry.webserver.http;

public enum HttpStatus {

    OK(200),
    FOUND(302),
    FORBIDDEN(403);

    private int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public String toString() {
        return code + " " + this.name();
    }
}
