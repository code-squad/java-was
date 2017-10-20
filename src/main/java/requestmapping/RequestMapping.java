package requestmapping;

import controller.Controller;
import util.HttpRequestUtils.RequestTypes;

public class RequestMapping {
    
    
    private Controller controller;
    private RequestTypes requestType;
    
    public RequestMapping(RequestTypes type, Controller controller) {
        this.requestType = type;
        this.controller = controller;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void setRequestTypes(RequestTypes type) {
        this.requestType = type;
    }
    public RequestTypes getRequestTypes() {
        return this.requestType;
    }
    public Controller getController() {
        return this.controller;
    }
    
    
   
}
