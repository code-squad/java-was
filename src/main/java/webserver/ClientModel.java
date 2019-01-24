package webserver;

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


}
