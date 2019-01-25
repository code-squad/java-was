package codesquad.webserver;

import codesquad.model.HttpSession;

public enum MethodParameter {

    COMMON,
    HTTP_SESSION;

    public static MethodParameter getMethodParameter(Object aInstance) throws Exception {
        if (aInstance instanceof HttpSession) return HTTP_SESSION;
        return COMMON;
    }
}
