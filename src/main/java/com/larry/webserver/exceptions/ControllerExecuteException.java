package com.larry.webserver.exceptions;

public class ControllerExecuteException extends RuntimeException{

    private CustomError error;

    public ControllerExecuteException(CustomError error) {
        this.error = error;
    }

    public CustomError getError() {
        return error;
    }
}
