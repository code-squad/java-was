package webserver.exceptions;

import java.lang.reflect.InvocationTargetException;

public class ExceptionHandler {

    public static void handle(InvocationTargetException e) {
        System.out.println(e.getCause());
    }
}
