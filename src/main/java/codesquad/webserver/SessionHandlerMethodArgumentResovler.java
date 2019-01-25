package codesquad.webserver;

import codesquad.model.HttpSession;
import codesquad.model.request.HttpRequest;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public class SessionHandlerMethodArgumentResovler implements HandlerMethodArgumentResolver {
    private static final Logger log = getLogger(SessionHandlerMethodArgumentResovler.class);

    @Override
    public boolean supportsParameter(Class<?> parameterType, HttpRequest httpRequest) throws Exception {
        return parameterType.newInstance() instanceof HttpSession;
    }

    @Override
    public Object resolveArgument(Object aInstance, HttpRequest httpRequest) {
        HttpSession httpSession = (HttpSession) aInstance;
        log.debug("httpRequest SID : {}", httpRequest.getSID());
        return httpSession.of(httpRequest.getSID());
    }
}
