package requestmapping;

import java.util.Map;

import controller.Controller;
import util.HttpRequestUtils.RequestTypes;

public class RequestMapping {

    private static RequestMapping requestMapping;

    private RequestMapping() {

    }

    public static RequestMapping getInstance() {
        if (requestMapping == null) {
            requestMapping = new RequestMapping();
        }
        return requestMapping;
    }

    private Map<RequestLine, Controller> controllerList;

    public Controller getController(RequestLine rq) {
        return controllerList.get(rq);
    }

    public void addController(RequestLine rq, Controller controller) {
        this.controllerList.put(rq, controller);
    }
}
