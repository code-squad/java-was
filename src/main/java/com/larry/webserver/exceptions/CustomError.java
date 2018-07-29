package com.larry.webserver.exceptions;

public class CustomError {

    String message;
    String redirectUrl;

    public CustomError(String message, String redirectUrl) {
        this.message = message;
        this.redirectUrl = redirectUrl;
    }
}
