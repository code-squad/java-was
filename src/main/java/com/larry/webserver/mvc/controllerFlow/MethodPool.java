package com.larry.webserver.mvc.controllerFlow;

import com.larry.webserver.http.RequestHandlerKey;
import com.larry.webserver.http.RequestHandlerValue;


public interface MethodPool {

    RequestHandlerValue getMethodValue(RequestHandlerKey httpMethodAndPath);
}
