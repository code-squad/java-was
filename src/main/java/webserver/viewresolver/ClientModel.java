package webserver.viewresolver;

import java.util.HashMap;
import java.util.Map;

public class ClientModel {

    private Map<String, Object> attributes = new HashMap<>();

    public ClientModel() {

    }

    public ClientModel addAttribute(String key, Object object) {
        attributes.put(key, object);
        return this;
    }


    public boolean hasMustache(String key) {
        return attributes.containsKey(key);
    }

    public Object obtainAttribute(String key) {
        if(attributes.containsKey(key)) {
            return attributes.get(key);
        }

        return null;
    }
}
