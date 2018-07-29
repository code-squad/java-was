package com.larry.webserver.exceptions;

import com.larry.webserver.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ExceptionHandler {

    public static Response handle(Request request, InvocationTargetException e) throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        CustomError error = ((ControllerExecuteException)(e.getCause())).getError();

        Response response = new Response();
        response.setHttpVersion(request.getHttpVersion());
        response.setStatue(HttpStatus.FORBIDDEN);
        response.setView(ModelAndView.viewOf(error.redirectUrl));

        return response;
    }
}
