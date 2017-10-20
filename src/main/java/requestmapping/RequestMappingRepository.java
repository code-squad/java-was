package requestmapping;

import java.util.Map;

import controller.Controller;


public class RequestMappingRepository {
    
    private Map<String, RequestMapping> requestMappingStorage;
    
    public void setRequest(String path, Controller controller) {
        
        this.requestMappingStorage.put(path, new RequestMapping(controller.getType(), controller));
    
    }
    
    public RequestMapping findPath(String path) {
        return this.requestMappingStorage.get(path);
    }
}
