package com.larry.webserver.exceptions;

public class UnAuthenticationException extends RuntimeException{
    public UnAuthenticationException(String message) {
        super(message);
    }
}
